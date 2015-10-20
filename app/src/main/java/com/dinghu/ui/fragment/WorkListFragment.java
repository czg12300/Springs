
package com.dinghu.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.ui.activity.MainActivity;
import com.dinghu.ui.adapter.CommonFragmentPagerAdapter;
import com.dinghu.ui.fragment.worklist.HistoryListFragment;
import com.dinghu.ui.fragment.worklist.TodayListFragment;
import com.dinghu.ui.fragment.worklist.TodoListFragment;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.fragment.BaseWorkerFragment;
import cn.common.ui.widgt.ChangeThemeUtils;
import cn.common.ui.widgt.MainTabViewPager;
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

    private IndicatorViewPager mIndicatorViewPager;

    private TextView mTvTitle;

    private boolean isMapMode = false;

    private MainTabViewPager mainTabViewPager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainTabViewPager = ((MainActivity) activity).getMainTabViewPager();
        mainTabViewPager.setCanScroll(false);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_work_list);
        ChangeThemeUtils.adjustStatusBar(findViewById(R.id.fl_title), getActivity());
        mRgMode = (RadioGroup) findViewById(R.id.rg_mode);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        initIndicatorView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mRgMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent it = new Intent(BroadcastActions.ACTION_CHANGE_MODE);
                if (checkedId == R.id.rb_mode_list) {
                    isMapMode = false;
                    mIndicatorViewPager.setCanScroll(true);
                    it.putExtra("IsMapMode", false);
                } else if (checkedId == R.id.rb_mode_map) {
                    isMapMode = true;
                    it.putExtra("IsMapMode", true);
                    mIndicatorViewPager.setCanScroll(false);
                }
                sendBroadcast(it);
            }
        });

    }

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
        mIndicatorViewPager.setIndicatorListener(new IndicatorViewPager.IIndicatorListener() {
            @Override
            public void onTabPageSelected(int position) {
                if (position == 0) {
                    mRgMode.setVisibility(View.VISIBLE);
                    mTvTitle.setVisibility(View.GONE);
                    if (isMapMode) {
                        mIndicatorViewPager.setCanScroll(false);
                    }
                } else {
                    mRgMode.setVisibility(View.GONE);
                    mTvTitle.setVisibility(View.VISIBLE);
                    mIndicatorViewPager.setCanScroll(true);
                }
                if (position == 2) {
                    mainTabViewPager.setCanScrollRight(true);
                } else {
                    mainTabViewPager.setCanScroll(false);
                }

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mIndicatorViewPager != null && mainTabViewPager != null) {
            if (mIndicatorViewPager.getCurrentItem() == 2) {
                mainTabViewPager.setCanScrollRight(true);
            } else {
                mainTabViewPager.setCanScroll(false);
            }
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
                list.add(HistoryListFragment.newInstance());
                return new CommonFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                        list);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
