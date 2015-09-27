
package com.dinghu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.ui.adapter.CommonFragmentPagerAdapter;
import com.dinghu.ui.fragment.worklist.HistoryListFragment;
import com.dinghu.ui.fragment.worklist.TodayListFragment;
import com.dinghu.ui.fragment.worklist.TodoListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.common.ui.fragment.BaseWorkerFragment;
import cn.common.ui.widgt.ChangeThemeUtils;
import cn.common.ui.widgt.indicator.IIndicator;
import cn.common.ui.widgt.indicator.IndicatorViewPager;
import cn.common.utils.DisplayUtil;

/**
 * 描述：工单页面
 *
 * @author jake
 * @since 2015/9/12 11:36
 */

public class WorkListFragment extends BaseWorkerFragment {


    public static WorkListFragment newInstance() {
        return new WorkListFragment();
    }

    private RadioGroup mRgMode;
    /**
     * 当前的fragment的id
     */
    private int currentTab = R.id.rb_mode_list;

    /**
     * 存放tab的fragment
     */

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_work_list);
        ChangeThemeUtils.adjustStatusBar(findViewById(R.id.fl_title), getActivity());
        mRgMode = (RadioGroup) findViewById(R.id.rg_mode);
        initIndicatorView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mRgMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                showCurrentFragment(checkedId);
                currentTab = checkedId;
                Intent it = new Intent(BroadcastActions.ACTION_CHANGE_MODE);
                if (checkedId == R.id.rb_mode_list) {
                    it.putExtra("IsMapMode", false);
                } else if (checkedId == R.id.rb_mode_map) {
                    it.putExtra("IsMapMode", true);
                }
                sendBroadcast(it);

            }
        });

    }

    private IndicatorViewPager mIndicatorViewPager;

    private void initIndicatorView() {
        mIndicatorViewPager = (IndicatorViewPager) findViewById(R.id.ivp);
        mIndicatorViewPager.setTabHeight(getDimension(R.dimen.title_height));
        mIndicatorViewPager.setTabChangeColor(true);
        mIndicatorViewPager.setTabBackgroundColor(Color.WHITE);
        mIndicatorViewPager.setTabTextColor(getColor(R.color.black_404040));
        mIndicatorViewPager.setTabSelectColor(getColor(R.color.green_00cd92));
        mIndicatorViewPager.setTabTextSize(getDimension(R.dimen.text_content));
        mIndicatorViewPager.setTabLineHeight(DisplayUtil.dip(4));
        mIndicatorViewPager.setAverage(false);
        mIndicatorViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void setupBroadcastActions(List<String> actions) {
        super.setupBroadcastActions(actions);
        actions.add(BroadcastActions.ACTION_CHANGE_MODE);
    }

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        super.handleBroadcast(context, intent);
        if (TextUtils.equals(intent.getAction(), BroadcastActions.ACTION_CHANGE_MODE)) {
            mIndicatorViewPager.setCanScroll(!intent.getBooleanExtra("IsMapMode", false));
        }
    }


    @Override
    protected void initData() {
        super.initData();
        mIndicatorViewPager.setIndicator(new IIndicator() {
            @Override
            public List<String> getLabelList() {
                List<String> list = new ArrayList<String>();
                list.add(getString(R.string.tab_todo_work_list));
                list.add(getString(R.string.tab_today_work_list));
                list.add(getString(R.string.tab_history_work_list));
                return list;
            }

            @Override
            public PagerAdapter getAdapter() {
                List<Fragment> list = new ArrayList<Fragment>();
                list.add(TodoListFragment.newInstance());
                list.add(TodayListFragment.newInstance());
                list.add(HistoryListFragment.newInstance(true));
                return new CommonFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                        list);
            }
        });
    }

}
