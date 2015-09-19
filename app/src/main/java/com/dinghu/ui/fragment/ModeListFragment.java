package com.dinghu.ui.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import com.dinghu.R;
import com.dinghu.ui.adapter.CommonFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.fragment.BaseWorkerFragment;
import cn.common.ui.widgt.indicator.IIndicator;
import cn.common.ui.widgt.indicator.IndicatorViewPager;
import cn.common.utils.DisplayUtil;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 12:11
 */
public class ModeListFragment extends BaseWorkerFragment {
    public static ModeListFragment newInstance() {
        return new ModeListFragment();
    }

    private IndicatorViewPager mIndicatorViewPager;

    @Override
    protected void initView() {
        mIndicatorViewPager = new IndicatorViewPager(getActivity());
        setContentView(mIndicatorViewPager);
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
                return new CommonFragmentPagerAdapter(getFragmentManager(), list);
            }
        });
    }
}
