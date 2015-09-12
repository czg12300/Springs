package com.dinghu.ui.fragment;

import android.widget.ListView;

import com.dinghu.R;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.ui.adapter.WorkListAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.fragment.BaseWorkerFragment;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public class TodayListFragment extends BaseWorkerFragment {
    public static TodayListFragment newInstance() {
        return new TodayListFragment();
    }

    private ListView mLvList;
    private WorkListAdapter mAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_list_todo);
        mLvList = (ListView) findViewById(R.id.lv_list);
        mAdapter = new WorkListAdapter(getActivity());
        mLvList.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter.setData(getTest());
    }

    private List<WorkListInfo> getTest() {
        List<WorkListInfo> list = new ArrayList<WorkListInfo>();

        for (int i = 0; i < 30; i++) {
            WorkListInfo info = new WorkListInfo();
            info.deliveryAddress = "天河区岑村红花岗松岗小区";
            info.requestTime = "14:30 - 15:30";
            info.deliveryStaff = "杨发胜";
            info.deliveryNum = "36700324" + i;
            list.add(info);
        }

        return list;
    }
}
