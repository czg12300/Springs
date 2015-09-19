
package com.dinghu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;

import com.dinghu.R;
import com.dinghu.SpringApplication;
import com.dinghu.data.BroadcastActions;
import com.dinghu.ui.adapter.CommonFragmentPagerAdapter;
import com.dinghu.ui.fragment.UserCenterFragment;
import com.dinghu.ui.fragment.WorkListFragment;
import com.dinghu.ui.widget.MainTabViewPager;
import com.dinghu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.activity.BaseWorkerFragmentActivity;
import cn.common.ui.widgt.TabRadioGroup;

public class MainActivity extends BaseWorkerFragmentActivity
        implements ViewPager.OnPageChangeListener, TabRadioGroup.OnCheckedChangeListener {

    private MainTabViewPager mVpContent;

    private TabRadioGroup mRgMenu;

    private RadioButton mRbWorkList;

    private RadioButton mRbUserCenter;

    private long lastClickTime;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long now = System.currentTimeMillis();
            if (now - lastClickTime > 2000) {
                ToastUtil.show("再按一次退出");
                lastClickTime = now;
            } else {
                SpringApplication.getInstance().exitApp();
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        clearFragment();
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
        mVpContent.setAdapter(
                new CommonFragmentPagerAdapter(getSupportFragmentManager(), list));
    }

    /**
     * 清除当前activity中的所有fragment,用于被回收时再次调用onCreate，避免多个fragment
     */
    private void clearFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            FragmentTransaction removeFt = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                if (fragment != null) {
                    removeFt.remove(fragments.get(i));
                }
            }
            removeFt.commit();
        }
    }


    @Override
    public void onPageScrollStateChanged(int status) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (mVpContent.getCurrentItem()) {
            case 0:
                mRbWorkList.setChecked(true);
                break;
            case 1:
                mRbUserCenter.setChecked(true);
                break;
        }
    }

    @Override
    public void setupBroadcastActions(List<String> actionList) {
        super.setupBroadcastActions(actionList);
        actionList.add(BroadcastActions.ACTION_MAIN_ACTIVITY_SELECT_TAB_LOCATE);
    }

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        super.handleBroadcast(context, intent);
        String action = intent.getAction();
        if (TextUtils.equals(action, BroadcastActions.ACTION_MAIN_ACTIVITY_SELECT_TAB_LOCATE)) {
            if (mVpContent != null) {
                mVpContent.setCurrentItem(0, false);
            }
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
}
