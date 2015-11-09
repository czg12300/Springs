
package com.dinghu.ui.fragment.worklist;

import com.dinghu.R;
import com.dinghu.ui.widget.StatusView;
import com.dinghu.ui.widget.xlistview.XListView;
import com.dinghu.utils.ToastUtil;

import android.os.Message;

import cn.common.ui.adapter.BaseListAdapter;
import cn.common.ui.fragment.BaseWorkerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private static final int MSG_UI_LOAD = 1000;

    protected XListView mLvList;

    private int mPageIndex = START_PAGE_INDEX;

    private int mPageSize = 10;

    protected BaseListAdapter<T> mAdapter;

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

    @Override
    protected void initView() {
        mStatusView = new StatusView(getActivity());
        mStatusView.setContentView(getContentViewId());
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
                sendEmptyBackgroundMessageDelayed(MSG_BACK_LOAD, 300);
            }
        });
        mStatusView.showLoadingView();
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    /**
     * 获取中间内容的布局文件，必须包含XListView
     *
     * @return
     */
    protected int getContentViewId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        if (msg.what == MSG_UI_LOAD) {
            List<T> list = (List<T>) msg.obj;
            if (list == null) {
                if (mPageIndex > START_PAGE_INDEX) {
                    mPageIndex--;
                }
                if (mAdapter.getCount() > 0) {
                    mStatusView.showContentView();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                } else {
                    mStatusView.showFailView();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                }
                ToastUtil.show(R.string.load_error);
            } else {
                mLvList.setRefreshTime(getCurrentTime());
                mLvList.stopRefresh();
                if (list.size() > 0) {
                    mStatusView.showContentView();
                    if (list.size() < mPageSize) {
                        mLvList.stopLoadMore(true);
                    } else {
                        mLvList.stopLoadMore(false);
                    }
                    if (mPageIndex > START_PAGE_INDEX) {
                        mAdapter.addAll(list);
                        // 必须先添加到adapter才调用onLoadMoreSucceed
                        onLoadMoreSucceed(list);
                    } else {
                        mAdapter.setData(list);
                        // 必须先添加到adapter才调用onRefreshSucceed
                        onRefreshSucceed(list);
                    }
                } else {
                    mStatusView.showNoDataView();
                }
            }
        }
    }

    /**
     * 刷新成功，可用于子类扩展
     *
     * @param list
     */
    protected void onRefreshSucceed(List<T> list) {
    }

    /**
     * 加载数据成功，可用于子类扩展
     *
     * @param list
     */
    protected void onLoadMoreSucceed(List<T> list) {
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM — dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        Message message = obtainUiMessage();
        message.what = MSG_UI_LOAD;
        message.obj = loadData();
        message.sendToTarget();
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
