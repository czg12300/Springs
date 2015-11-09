
package com.dinghu.ui.activity;

import com.dinghu.R;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.AccountResponse;
import com.dinghu.ui.helper.DateSelectorHelper;
import com.dinghu.ui.helper.LoadingDialogHelper;
import com.dinghu.ui.widget.StatusView;

import android.os.Message;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 描述：我的账本页面
 *
 * @author jake
 * @since 2015/9/20 10:28
 */
public class MyAccountBookActivity extends CommonTitleActivity {
    private static final int MSG_BACK_LOAD = 0;

    private static final int MSG_UI_LOAD = 1;

    private TextView mTvWorkListCount;

    private TextView mTvSendCount;

    private TextView mTvNullCount;

    private TextView mTvLongCount;

    private TextView mTvHeightCount;

    private TextView mTvWorkListCount1;

    private TextView mTvReceiveCount;

    private TextView mTvPayCount;

    private TextView mTvStoreName;

    private StatusView mStatusView;

    private LoadingDialogHelper mLoadingDialogHelper;

    private DateSelectorHelper mDateSelectorHelper;

    @Override
    protected void initView() {
        setTitle(R.string.title_my_account_book);
        setContentView(R.layout.activity_my_account);
        mStatusView = (StatusView) findViewById(R.id.status_view);
        mStatusView.setContentView(R.layout.view_accont_book_content);
        mTvWorkListCount = (TextView) findViewById(R.id.tv_work_list_count);
        mTvSendCount = (TextView) findViewById(R.id.tv_send_count);
        mTvNullCount = (TextView) findViewById(R.id.tv_null_count);
        mTvLongCount = (TextView) findViewById(R.id.tv_long_count);
        mTvHeightCount = (TextView) findViewById(R.id.tv_height_count);
        mTvWorkListCount1 = (TextView) findViewById(R.id.tv_work_list_count2);
        mTvReceiveCount = (TextView) findViewById(R.id.tv_receive_count);
        mTvPayCount = (TextView) findViewById(R.id.tv_pay_count);
        mTvStoreName = (TextView) findViewById(R.id.tv_store_name);
        mStatusView.showLoadingView();
        mDateSelectorHelper = new DateSelectorHelper(this);
        mLoadingDialogHelper = new LoadingDialogHelper(this);
        FrameLayout layout = (FrameLayout) findViewById(R.id.fl_header);
        layout.addView(mDateSelectorHelper.getView());
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        if (msg.what == MSG_BACK_LOAD) {
            HttpRequestManager<AccountResponse> requestManager = new HttpRequestManager<AccountResponse>(
                    URLConfig.ACCOUNT_BOOK, AccountResponse.class);
            requestManager.addParam("id", InitShareData.getUserId() + "");
            if (mDateSelectorHelper != null) {
                requestManager.addParam("startDate", mDateSelectorHelper.getStartDate());
                requestManager.addParam("endDate", mDateSelectorHelper.getEndDate());
            }
            Message message = obtainUiMessage();
            message.obj = requestManager.sendRequest();
            message.what = MSG_UI_LOAD;
            message.sendToTarget();
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        if (msg.what == MSG_UI_LOAD) {
            mLoadingDialogHelper.hide();
            if (msg.obj != null) {
                mStatusView.showContentView();
                AccountResponse response = (AccountResponse) msg.obj;
                if (response != null && response.isOk()) {
                    mTvWorkListCount.setText("" + response.getPs_formTotal());
                    mTvSendCount.setText("" + response.getPs_ssCount());
                    mTvNullCount.setText("" + response.getPs_emptyCount());
                    mTvLongCount.setText("" + response.getPs_ct());
                    mTvHeightCount.setText("" + response.getPs_gc());
                    mTvWorkListCount1.setText("" + response.getTc_formTotal());
                    mTvReceiveCount.setText("" + response.getTc_skAmount());
                    mTvPayCount.setText("" + response.getTc_jkAmount());
                    mTvStoreName.setText("" + response.getStoreName());
                } else {
                    mStatusView.showFailView();
                }
            } else {
                mStatusView.showFailView();
            }

        }
    }

    @Override
    protected void initEvent() {
        mStatusView.setStatusListener(new StatusView.StatusListener() {
            @Override
            public void onLoad() {
                sendEmptyBackgroundMessageDelayed(MSG_BACK_LOAD, 300);
            }
        });
        mDateSelectorHelper.setListener(new DateSelectorHelper.IListener() {

            @Override
            public void loadData() {
                mLoadingDialogHelper.show();
                sendEmptyBackgroundMessage(MSG_BACK_LOAD);
            }
        });
    }

}
