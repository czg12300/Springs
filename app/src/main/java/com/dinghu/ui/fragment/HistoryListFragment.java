
package com.dinghu.ui.fragment;

import android.view.View;

import com.dinghu.R;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.widget.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.adapter.BaseListAdapter;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public class HistoryListFragment extends BaseListFragment<WorkListInfo> {
    public static HistoryListFragment newInstance() {
        return new HistoryListFragment();
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
        View header = inflate(R.layout.header_list_history);
        mLvList.addHeaderView(header);
    }

    @Override
    protected BaseListAdapter<WorkListInfo> createAdapter() {
        return new WorkListAdapter(getActivity());
    }
}
