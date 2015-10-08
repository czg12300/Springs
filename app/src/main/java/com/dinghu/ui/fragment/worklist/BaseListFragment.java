
package com.dinghu.ui.fragment.worklist;

import android.os.Message;

import com.dinghu.R;
import com.dinghu.ui.widget.StatusView;
import com.dinghu.ui.widget.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.common.ui.adapter.BaseListAdapter;
import cn.common.ui.fragment.BaseWorkerFragment;

/**
 * 描述：未完工工单页面
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public abstract class BaseListFragment<T> extends BaseWorkerFragment
        implements XListView.IXListViewListener {

    private static final int START_PAGE_INDEX = 1;

    private static final int MSG_BACK_LOAD = 1000;

    private static final int MSG_UI_LOAD_FAIL = 1001;

    protected static final int MSG_UI_LOAD_SUCCESS = 1002;

    private static final int MSG_UI_FINISH_LOAD_ALL = 1003;
    private static final int MSG_UI_NO_DATA = 1004;

    protected XListView mLvList;

    private int mPageIndex = START_PAGE_INDEX;

    private int mPageSize = 10;

    private BaseListAdapter<T> mAdapter;

    protected void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    protected int getPageIndex() {
        return mPageIndex;
    }

    protected int getPageSize() {
        return mPageSize;
    }

    private StatusView mStatusView;

    private boolean isInit = false;

    @Override
    protected void initView() {
        mStatusView = new StatusView(getActivity());
        mStatusView.setContentView(R.layout.fragment_base_list);
        setContentView(mStatusView);
        mLvList = (XListView) findViewById(R.id.lv_list);
        mLvList.setPullLoadEnable(true);
        addHeader(mLvList);
        mLvList.setXListViewListener(this);
        mAdapter = createAdapter();
        mLvList.setAdapter(mAdapter);
        addFooter(mLvList);
        mStatusView.setStatusListener(new StatusView.StatusListener() {
            @Override
            public void onLoad() {
                sendEmptyBackgroundMessageDelayed(MSG_BACK_LOAD, 2000);
            }
        });
        mStatusView.showLoadingView();
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInit) {
            if (mStatusView != null) {

                isInit = true;
            }
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case MSG_UI_LOAD_FAIL:
                if (mAdapter.getCount() == 0 && mPageIndex == START_PAGE_INDEX) {
                    mStatusView.showFailView();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                } else if (mPageIndex == START_PAGE_INDEX) {
                    mStatusView.showContentView();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                } else {
                    mStatusView.showContentView();
                    mLvList.stopLoadMore(false);
                }
                break;
            case MSG_UI_LOAD_SUCCESS:
                mStatusView.showContentView();
                List<T> list = (List<T>) msg.obj;
                if (mPageIndex == START_PAGE_INDEX) {
                    mAdapter.setData(list);
                    mLvList.setRefreshTime(getCurrentTime());
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                } else {
                    mLvList.setRefreshTime(getCurrentTime());
                    mLvList.stopRefresh();
                    mAdapter.addAll(list);
                    mLvList.stopLoadMore(false);
                }
                break;
            case MSG_UI_FINISH_LOAD_ALL:
                mStatusView.showContentView();
                if (mPageIndex == START_PAGE_INDEX) {
                    mAdapter.setData((List<T>) msg.obj);
                    mLvList.setRefreshTime(getCurrentTime());
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(true);
                } else {
                    mAdapter.addAll((List<T>) msg.obj);
                    mLvList.stopLoadMore(true);
                }
                break;
            case MSG_UI_NO_DATA:
                mStatusView.showNoDataView();
                mLvList.stopRefresh();
                mLvList.stopLoadMore(false);
                break;
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM — dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        List<T> list = loadData();
        if (list != null && list.size() == 0 && getPageIndex() == START_PAGE_INDEX) {
            sendEmptyUiMessage(MSG_UI_NO_DATA);
        } else if (list != null && list.size() >= mPageSize) {
            Message message = obtainUiMessage();
            message.what = MSG_UI_LOAD_SUCCESS;
            message.obj = list;
            message.sendToTarget();
        } else if (mPageIndex > START_PAGE_INDEX && list == null
                || list != null && list.size() < mPageSize) {
            Message message = obtainUiMessage();
            message.what = MSG_UI_FINISH_LOAD_ALL;
            message.obj = list;
            message.sendToTarget();
        } else if (list == null && mPageIndex == START_PAGE_INDEX) {
            sendEmptyUiMessage(MSG_UI_LOAD_FAIL);
        }

    }

    @Override
    public void onRefresh() {
        mPageIndex = START_PAGE_INDEX;
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);

    }

    @Override
    public void onLoadMore() {
        mPageIndex++;
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    protected abstract List<T> loadData();

    protected abstract void addFooter(XListView mLvList);

    protected abstract void addHeader(XListView mLvList);

    protected abstract BaseListAdapter<T> createAdapter();
}
