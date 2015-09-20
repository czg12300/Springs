package com.dinghu.ui.activity;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.FinishWorkResponse;
import com.dinghu.logic.http.response.WorkListDetailResponse;
import com.dinghu.ui.widget.StatusView;
import com.dinghu.ui.widget.WorkListDetailItemView;
import com.dinghu.utils.ToastUtil;

/**
 * 描述：工单详情页面
 *
 * @author jake
 * @since 2015/9/20 10:28
 */
public class WorkListDetailActivity extends CommonTitleActivity {
    private static final int MSG_BACK_LOAD = 0;
    private static final int MSG_BACK_RESPONSE_QUHUO = 1;
    private static final int MSG_BACK_RESPONSE_WANGONG = 2;
    private static final int MSG_UI_LOAD = 100;
    private static final int MSG_UI_RESPONSE_QUHUO = 101;
    private static final int MSG_UI_RESPONSE_WANGONG = 102;
    private StatusView mStatusView;
    private WorkListDetailItemView mItemRequestTime;
    private WorkListDetailItemView mItemName;
    private WorkListDetailItemView mItemTel;
    private WorkListDetailItemView mItemAddress;
    private WorkListDetailItemView mItemGoods;
    private WorkListDetailItemView mItemType;
    private WorkListDetailItemView mItemNum;
    private Button mBtnOk;
    private int status = -1;

    @Override
    protected void initView() {
        setTitle(R.string.title_work_list_detail);
        mStatusView = new StatusView(this);
        mStatusView.setContentView(R.layout.activity_work_list_detail);
        setContentView(mStatusView);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mItemRequestTime = (WorkListDetailItemView) findViewById(R.id.div_request_time);
        mItemName = (WorkListDetailItemView) findViewById(R.id.div_name);
        mItemTel = (WorkListDetailItemView) findViewById(R.id.div_mobile);
        mItemAddress = (WorkListDetailItemView) findViewById(R.id.div_address);
        mItemGoods = (WorkListDetailItemView) findViewById(R.id.div_goods);
        mItemType = (WorkListDetailItemView) findViewById(R.id.div_type);
        mItemNum = (WorkListDetailItemView) findViewById(R.id.div_num);
        mItemRequestTime.setLabel("要求时间：");
        mItemName.setLabel("姓名：");
        mItemTel.setLabel("电话：");
        mItemAddress.setLabel("地址：");
        mItemGoods.setLabel("产品：");
        mItemType.setLabel("类型：");
        mItemNum.setLabel("数量：");
        mStatusView.showLoadingView();
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mStatusView.setStatusListener(new StatusView.StatusListener() {
            @Override
            public void onLoad() {
                sendEmptyBackgroundMessage(MSG_BACK_LOAD);
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status != -1) {
                    switch (status) {
                        case WorkListDetailResponse.STATUS_QUHUO:
                            sendEmptyBackgroundMessage(MSG_BACK_RESPONSE_QUHUO);
                            break;
                        case WorkListDetailResponse.STATUS_WANGONG:
                            sendEmptyBackgroundMessage(MSG_BACK_RESPONSE_WANGONG);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        switch (msg.what) {
            case MSG_BACK_LOAD:
                HttpRequestManager<WorkListDetailResponse> requestLoad = new HttpRequestManager<WorkListDetailResponse>(URLConfig.WORK_LIST_DETAIL, WorkListDetailResponse.class);
                requestLoad.addParam("id", getWorkListId() + "");
                Message msgLoad = obtainUiMessage();
                msgLoad.what = MSG_UI_LOAD;
                msgLoad.obj = requestLoad.sendRequest();
                msgLoad.sendToTarget();
                break;
            case MSG_BACK_RESPONSE_QUHUO:
                HttpRequestManager<FinishWorkResponse> requestQH = new HttpRequestManager<FinishWorkResponse>(URLConfig.DETAIL_GAINGOODS, FinishWorkResponse.class);
                requestQH.addParam("id", getWorkListId() + "");
                Message msgQH = obtainUiMessage();
                msgQH.what = MSG_UI_RESPONSE_QUHUO;
                msgQH.obj = requestQH.sendRequest();
                msgQH.sendToTarget();
                break;
            case MSG_BACK_RESPONSE_WANGONG:
                HttpRequestManager<FinishWorkResponse> requestWG = new HttpRequestManager<FinishWorkResponse>(URLConfig.DETAIL_SENDGOODS, FinishWorkResponse.class);
                requestWG.addParam("id", getWorkListId() + "");
                Message msgWG = obtainUiMessage();
                msgWG.what = MSG_UI_RESPONSE_WANGONG;
                msgWG.obj = requestWG.sendRequest();
                msgWG.sendToTarget();
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        if (msg.what == MSG_UI_LOAD) {
            if (msg.obj != null && msg.obj instanceof WorkListDetailResponse) {
                mStatusView.showContentView();
                WorkListDetailResponse info = (WorkListDetailResponse) msg.obj;
                if (info != null) {
                    mItemRequestTime.setContent(info.getTime());
                    mItemName.setContent(info.getName());
                    mItemTel.setContent(info.getTel());
                    mItemAddress.setContent(info.getAddress());
                    mItemGoods.setContent(info.getTime());
                    mItemType.setContent(info.getTime());
                    if (TextUtils.equals(info.getType(), WorkListInfo.TYPE_TAOCAN)) {
                        mItemNum.setLabel("金额：");
                        mItemNum.setContent(info.getMoneyOrCount() + "元");
                    } else if (TextUtils.equals(info.getType(), WorkListInfo.TYPE_PEISONG)) {
                        mItemNum.setLabel("数量：");
                        mItemNum.setContent(info.getMoneyOrCount() + "桶");
                    }
                    status = info.getStatus();
                    if (info.getStatus() == WorkListDetailResponse.STATUS_WAIT) {
                        mBtnOk.setEnabled(false);
                    } else {
                        mBtnOk.setEnabled(true);
                    }
                    mBtnOk.setText(info.getBtnMsg());
                }
            } else {
                mStatusView.showFailView();
            }
        } else if (msg.what == MSG_UI_RESPONSE_QUHUO || msg.what == MSG_UI_RESPONSE_WANGONG) {
            if (msg.obj != null && msg.obj instanceof FinishWorkResponse) {
                FinishWorkResponse response = (FinishWorkResponse) msg.obj;
                if (response.getCode() == FinishWorkResponse.CODE_SUCCESS) {
                    finish();
                    sendBroadcast(BroadcastActions.ACTION_UPDATE_TODO_WORK_LIST);
                }
                ToastUtil.show(response.getMsg());
            } else {
                ToastUtil.show(R.string.load_error);
            }
        }
    }

    private long getWorkListId() {
        return getIntent().getLongExtra("WorkListId", -1);
    }
}