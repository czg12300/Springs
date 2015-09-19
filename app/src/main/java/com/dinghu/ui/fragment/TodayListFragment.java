
package com.dinghu.ui.fragment;

import com.dinghu.R;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.widget.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.adapter.BaseListAdapter;
import cn.common.ui.fragment.BaseWorkerFragment;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public class TodayListFragment extends BaseListFragment<WorkListInfo> {
    public static TodayListFragment newInstance() {
        return new TodayListFragment();
    }

    @Override
    protected List<WorkListInfo> loadData() {
        return null;
    }

    @Override
    protected void addFooter(XListView mLvList) {

    }

    @Override
    protected void addHeader(XListView mLvList) {

    }

    @Override
    protected BaseListAdapter<WorkListInfo> createAdapter() {
        return new WorkListAdapter(getActivity());
    }

    private List<WorkListInfo> getTest() {
        List<WorkListInfo> list = new ArrayList<WorkListInfo>();

        for (int i = 0; i < 3; i++) {
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
