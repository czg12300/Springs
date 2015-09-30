
package com.dinghu.ui.fragment.worklist;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.widget.xlistview.XListView;

import java.util.List;

import cn.common.ui.adapter.BaseListAdapter;

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
    public void setupBroadcastActions(List<String> actions) {
        super.setupBroadcastActions(actions);
        actions.add(BroadcastActions.ACTION_UPDATE_TODO_WORK_LIST);
    }

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        super.handleBroadcast(context, intent);
        String action = intent.getAction();
        if (TextUtils.equals(action, BroadcastActions.ACTION_UPDATE_TODO_WORK_LIST)) {
            onRefresh();
        }
    }

    @Override
    protected List<WorkListInfo> loadData() {
        HttpRequestManager<WorkListResponse> request = new HttpRequestManager<WorkListResponse>(
                URLConfig.WORK_LIST_TODAY, WorkListResponse.class);
        request.addParam("pageNum", getPageIndex() + "");
        request.addParam("pageSize", getPageSize() + "");
        request.addParam("employId", InitShareData.getUserId() + "");
        WorkListResponse response = request.sendRequest();
        if (response != null) {
            return response.getList();
        }
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

}
