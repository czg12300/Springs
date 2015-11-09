
package com.dinghu.ui.fragment.worklist;

import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.helper.DateSelectorHelper;
import com.dinghu.ui.helper.LoadingDialogHelper;
import com.dinghu.ui.widget.xlistview.XListView;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import cn.common.ui.adapter.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：历史工单页面
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public class HistoryListFragment extends BaseListFragment<WorkListInfo> {

    public static HistoryListFragment newInstance() {
        return new HistoryListFragment();
    }

    private DateSelectorHelper mDateSelectorHelper;

    private LoadingDialogHelper mLoadingDialogHelper;

    @Override
    protected List<WorkListInfo> loadData() {
        HttpRequestManager<WorkListResponse> request = new HttpRequestManager<WorkListResponse>(
                URLConfig.WORK_LIST_HISTORY, WorkListResponse.class);
        request.addParam("pageNum", getPageIndex() + "");
        request.addParam("pageSize", getPageSize() + "");
        request.addParam("employId", InitShareData.getUserId() + "");
        if (mDateSelectorHelper != null) {
            request.addParam("startDate", mDateSelectorHelper.getStartDate());
            request.addParam("endDate", mDateSelectorHelper.getEndDate());
        }
        WorkListResponse response = request.sendRequest();
        if (response != null) {
            if (response.isOk() && response.getList() == null) {
                return new ArrayList<WorkListInfo>();
            }
            return response.getList();
        }
        return null;
    }

    @Override
    public void setContentView(View view) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        mDateSelectorHelper = new DateSelectorHelper(getActivity());
        mLoadingDialogHelper = new LoadingDialogHelper(getActivity());
        layout.addView(mDateSelectorHelper.getView());
        layout.addView(view, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        super.setContentView(layout);
        mDateSelectorHelper.setListener(new DateSelectorHelper.IListener() {
            @Override
            public void loadData() {
                mLoadingDialogHelper.show();
                onRefresh();
            }
        });
    }

    @Override
    protected void addFooter(XListView mLvList) {
    }

    @Override
    protected void addHeader(XListView mLvList) {
    }

    @Override
    protected BaseListAdapter<WorkListInfo> createAdapter() {
        return new WorkListAdapter(getActivity(), WorkListAdapter.TYPE_HISTORY);
    }

    @Override
    public void setupBroadcastActions(List<String> actions) {
        super.setupBroadcastActions(actions);
        actions.add(BroadcastActions.ACTION_UPDATE_HISTORY_WORK_LIST);
    }

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(),
                BroadcastActions.ACTION_UPDATE_HISTORY_WORK_LIST)) {
            onRefresh();
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        mLoadingDialogHelper.hide();
        super.handleUiMessage(msg);

    }

}
