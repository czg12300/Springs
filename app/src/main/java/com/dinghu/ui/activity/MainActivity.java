
package com.dinghu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.RadioButton;

import com.dinghu.R;
import com.dinghu.SpringApplication;
import com.dinghu.data.BroadcastActions;
import com.dinghu.ui.adapter.CommonFragmentPagerAdapter;
import com.dinghu.ui.fragment.UserCenterFragment;
import com.dinghu.ui.fragment.WorkListFragment;
import com.dinghu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.activity.BaseWorkerFragmentActivity;
import cn.common.ui.widgt.MainTabViewPager;
import cn.common.ui.widgt.TabRadioGroup;

public class MainActivity extends BaseWorkerFragmentActivity
        implements ViewPager.OnPageChangeListener, TabRadioGroup.OnCheckedChangeListener {

    private MainTabViewPager mVpContent;

    private TabRadioGroup mRgMenu;

    private RadioButton mRbWorkList;

    private RadioButton mRbUserCenter;

    private long lastClickTime;

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - lastClickTime > 2000) {
            ToastUtil.show("再按一次退出");
            lastClickTime = now;
        } else {
            SpringApplication.getInstance().exitApp();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVpContent = (MainTabViewPager) findViewById(R.id.vp_content);
        mRgMenu = (TabRadioGroup) findViewById(R.id.rg_menu);
        mRbWorkList = (RadioButton) findViewById(R.id.rb_work_list);
        mRbUserCenter = (RadioButton) findViewById(R.id.rb_user_center);
        mVpContent.setOnPageChangeListener(this);
        mRgMenu.setOnCheckedChangeListener(this);
        ArrayList<Fragment> list = new ArrayList<Fragment>();
        list.add(WorkListFragment.newInstance());
        list.add(UserCenterFragment.newInstance());
        mVpContent.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), list));
        mVpContent.setCanScroll(true);
    }

    public MainTabViewPager getMainTabViewPager() {
        return mVpContent;
    }

    @Override
    public void onPageScrollStateChanged(int status) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mRbWorkList.setChecked(true);
                break;
            case 1:
                mVpContent.setCanScrollLeft(true);
                mRbUserCenter.setChecked(true);
                break;
        }
    }

    @Override
    public void setupBroadcastActions(List<String> actionList) {
        super.setupBroadcastActions(actionList);
        actionList.add(BroadcastActions.ACTION_MAIN_ACTIVITY_SELECT_TAB_LOCATE);
        actionList.add(BroadcastActions.ACTION_EXIT_TO_LOGIN);
    }

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        super.handleBroadcast(context, intent);
        String action = intent.getAction();
        if (TextUtils.equals(action, BroadcastActions.ACTION_MAIN_ACTIVITY_SELECT_TAB_LOCATE)) {
            if (mVpContent != null) {
                mVpContent.setCurrentItem(0, false);
            }
        } else if (TextUtils.equals(action, BroadcastActions.ACTION_EXIT_TO_LOGIN)) {
            finish();
        }
    }

    @Override
    public void onCheckedChanged(TabRadioGroup group, int checkedId) {
        int position = 0;
        switch (checkedId) {
            case R.id.rb_work_list:
                position = 0;
                break;
            case R.id.rb_user_center:
                position = 1;
                break;
        }
        mVpContent.setCurrentItem(position, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }
}
