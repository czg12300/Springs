
package com.dinghu.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import cn.common.ui.BaseDialog;

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
    private static final int MSG_UI_FINISH = 103;

    private StatusView mStatusView;

    private WorkListDetailItemView mItemRequestTime;

    private WorkListDetailItemView mItemName;

    private WorkListDetailItemView mItemAddress;

    private WorkListDetailItemView mItemGoods;

    private WorkListDetailItemView mItemType;

    private WorkListDetailItemView mItemNum;

    private TextView tvMobile;

    private TextView tvSpinnerLabel;

    private TextView tvSpinner;

    private ImageView ivSpinnerAdd;

    private ImageView ivSpinnerSub;

    private ImageView ivMobile;

    private Button mBtnOk;

    private int status = -1;

    private WorkListDetailResponse mInfo;

    private long workListId = -1;

    private boolean isFinishWorkList = false;
    private BaseDialog mLoadingDialog;

    @Override
    protected void initView() {
        setTitle(R.string.title_work_list_detail);
        mStatusView = new StatusView(this);
        mStatusView.setContentView(R.layout.activity_work_list_detail);
        setContentView(mStatusView);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        tvSpinnerLabel = (TextView) findViewById(R.id.tv_spinner_label);
        tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        ivSpinnerAdd = (ImageView) findViewById(R.id.iv_spinner_add);
        ivSpinnerSub = (ImageView) findViewById(R.id.iv_spinner_sub);
        mItemRequestTime = (WorkListDetailItemView) findViewById(R.id.div_request_time);
        mItemName = (WorkListDetailItemView) findViewById(R.id.div_name);
        mItemAddress = (WorkListDetailItemView) findViewById(R.id.div_address);
        mItemGoods = (WorkListDetailItemView) findViewById(R.id.div_goods);
        mItemType = (WorkListDetailItemView) findViewById(R.id.div_type);
        mItemNum = (WorkListDetailItemView) findViewById(R.id.div_num);
        mItemRequestTime.setLabel("要求时间：");
        mItemName.setLabel("姓名：");
        tvMobile = (TextView) findViewById(R.id.tv_phone);
        ivMobile = (ImageView) findViewById(R.id.iv_phone);
        mItemAddress.setLabel("地址：");
        mItemGoods.setLabel("产品：");
        mItemType.setLabel("类型：");
        mItemNum.setLabel("数量：");
        workListId = getIntent().getLongExtra("WorkListId", -1);
        isFinishWorkList = getIntent().getBooleanExtra("IsNotTodoWorkList", false);
        if (isFinishWorkList) {
            tvSpinner.setBackgroundColor(Color.TRANSPARENT);
            ivSpinnerAdd.setVisibility(View.GONE);
            ivSpinnerSub.setVisibility(View.GONE);
        }
        mStatusView.showLoadingView();
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        ivSpinnerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfo != null) {
                    mInfo.setMoneyOrCount2(mInfo.getMoneyOrCount2() + 1);
                    tvSpinner.setText(mInfo.getMoneyOrCount2() + "");
                }
            }
        });
        ivSpinnerSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfo != null) {
                    if (mInfo.getMoneyOrCount2() - 1 > 0) {
                        mInfo.setMoneyOrCount2(mInfo.getMoneyOrCount2() - 1);
                        tvSpinner.setText(mInfo.getMoneyOrCount2() + "");
                    }
                }
            }
        });
        ivMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCustomerService();
            }
        });
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
                    showLoadingDialog();
                }
            }
        });
    }

    /**
     * 打电话
     */
    private void callCustomerService() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tvMobile.getText()));
        startActivity(intent);
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        switch (msg.what) {
            case MSG_BACK_LOAD:
                HttpRequestManager<WorkListDetailResponse> requestLoad = new HttpRequestManager<WorkListDetailResponse>(
                        URLConfig.WORK_LIST_DETAIL, WorkListDetailResponse.class);
                requestLoad.addParam("id", workListId + "");
                Message msgLoad = obtainUiMessage();
                msgLoad.what = MSG_UI_LOAD;
                msgLoad.obj = requestLoad.sendRequest();
                msgLoad.sendToTarget();
                break;
            case MSG_BACK_RESPONSE_QUHUO:
                HttpRequestManager<FinishWorkResponse> requestQH = new HttpRequestManager<FinishWorkResponse>(
                        URLConfig.DETAIL_GAINGOODS, FinishWorkResponse.class);
                requestQH.addParam("id", workListId + "");
                Message msgQH = obtainUiMessage();
                msgQH.what = MSG_UI_RESPONSE_QUHUO;
                msgQH.obj = requestQH.sendRequest();
                msgQH.sendToTarget();
                break;
            case MSG_BACK_RESPONSE_WANGONG:
                HttpRequestManager<FinishWorkResponse> requestWG = new HttpRequestManager<FinishWorkResponse>(
                        URLConfig.DETAIL_SENDGOODS, FinishWorkResponse.class);
                requestWG.addParam("id", workListId + "");
                int count = 0;
                if (mInfo != null) {
                    count = mInfo.getMoneyOrCount2();
                }
                requestWG.addParam("count", count + "");
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
                mInfo = (WorkListDetailResponse) msg.obj;
                if (mInfo != null) {
                    mItemRequestTime.setContent(mInfo.getTime());
                    mItemName.setContent(mInfo.getName());
                    if (!TextUtils.isEmpty(mInfo.getTel())) {
                        tvMobile.setText(mInfo.getTel());
                        ivMobile.setVisibility(View.VISIBLE);
                    } else {
                        ivMobile.setVisibility(View.GONE);
                    }
                    mItemAddress.setContent(mInfo.getAddress());
                    mItemGoods.setContent(mInfo.getGoods());
                    mItemType.setContent(mInfo.getType());
                    int count = 0;
                    if (mInfo != null) {
                        count = mInfo.getMoneyOrCount2();
                    }
                    if (TextUtils.equals(mInfo.getType(), WorkListInfo.TYPE_TAOCAN)) {
                        mItemNum.setLabel("金额：");
                        mItemNum.setContent(mInfo.getMoneyOrCount() + "元");
                        tvSpinnerLabel.setText("本次收款：");
                        if (isFinishWorkList) {
                            tvSpinner.setText("" + count + "元");
                        } else {
                            tvSpinner.setText("" + count);
                        }
                    } else if (TextUtils.equals(mInfo.getType(), WorkListInfo.TYPE_PEISONG)) {
                        mItemNum.setLabel("数量：");
                        mItemNum.setContent(mInfo.getMoneyOrCount() + "桶");
                        tvSpinnerLabel.setText("回收空桶：");
                        if (isFinishWorkList) {
                            tvSpinner.setText("" + count + "桶");
                        } else {
                            tvSpinner.setText("" + count);
                        }
                    }
                    status = mInfo.getStatus();
                    if (mInfo.getStatus() == WorkListDetailResponse.STATUS_WAIT) {
                        mBtnOk.setEnabled(false);
                    } else {
                        mBtnOk.setEnabled(true);
                    }
                    mBtnOk.setText(mInfo.getBtnMsg());
                }
            } else {
                mStatusView.showFailView();
            }
        } else if (msg.what == MSG_UI_RESPONSE_QUHUO || msg.what == MSG_UI_RESPONSE_WANGONG) {
            if (msg.obj != null && msg.obj instanceof FinishWorkResponse) {
                FinishWorkResponse response = (FinishWorkResponse) msg.obj;
                if (response.getCode() == FinishWorkResponse.CODE_SUCCESS) {
                    sendBroadcast(BroadcastActions.ACTION_UPDATE_TODO_WORK_LIST);
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    sendEmptyUiMessageDelayed(MSG_UI_FINISH, 1000);
                }
                ToastUtil.show(response.getMsg());
            } else {
                ToastUtil.show(R.string.load_error);
            }
        } else if (msg.what == MSG_UI_FINISH) {
            finish();
        }
    }

    public void showLoadingDialog() {
        if (!isFinishing()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new BaseDialog(this);
                mLoadingDialog.setWindow(R.style.alpha_animation, 0.0f);
                mLoadingDialog.setContentView(R.layout.dialog_loading);
            }
            mLoadingDialog.show();
        }
    }
}
