
package com.dinghu.ui.activity;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.FinishWorkResponse;
import com.dinghu.logic.http.response.WorkListDetailResponse;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.helper.LoadingDialogHelper;
import com.dinghu.ui.widget.AndroidBug5497Workaround;
import com.dinghu.ui.widget.StatusView;
import com.dinghu.ui.widget.WorkListDetailItemView;
import com.dinghu.utils.ToastUtil;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.common.ui.BaseDialog;

/**
 * 描述：工单详情页面
 *
 * @author jake
 * @since 2015/9/20 10:28
 */
public class WorkListDetailActivity extends CommonTitleActivity {
    private static final int MSG_BACK_LOAD = 0;

    private static final int MSG_BACK_UN_FINISH_WORK = 1;

    private static final int MSG_BACK_FINISH_WORK = 2;

    private static final int MSG_BACK_CANCEL = 3;

    private static final int MSG_UI_LOAD = 0;

    private static final int MSG_UI_FINISH = 1;

    private static final int MSG_UI_UN_FINISH_WORK = 2;

    private static final int MSG_UI_FINISH_WORK = 3;

    private static final int MSG_UI_CANCEL = 4;

    private static final long FINISH_DELAY_TIME = 500;

    private StatusView mStatusView;

    private WorkListDetailItemView mItemRequestTime;

    private WorkListDetailItemView mItemName;

    private WorkListDetailItemView mItemAddress;

    private WorkListDetailItemView mItemGoods;

    private WorkListDetailItemView mItemType;

    private WorkListDetailItemView mItemNum;

    private WorkListDetailItemView mItemReport;

    private TextView tvMobile;

    private TextView tvSpinnerLabel;

    private EditText evSpinner;

    private ImageView ivSpinnerAdd;

    private ImageView ivSpinnerSub;

    private ImageView ivMobile;

    private View vButton;

    private Button mBtnUnFinish;

    private Button mBtnFinish;

    private Button mBtnOpt;

    private WorkListDetailResponse mInfo;

    private long workListId = -1;

    private int type;

    private BaseDialog mFinishDialog;

    private BaseDialog mUnFinishDialog;

    private BaseDialog mCancelDialog;

    private EditText mEvFinishReport;

    private EditText mEvUnFinishReport;

    private TextView mTvCancelTitle;

    private LoadingDialogHelper mLoadingDialogHelper;

    @Override
    protected void initView() {
        setTitle(R.string.title_work_list_detail);
        mStatusView = new StatusView(this);
        mStatusView.setContentView(R.layout.activity_work_list_detail);
        setContentView(mStatusView);
        FrameLayout content = (FrameLayout) findViewById(android.R.id.content);
        content.setBackgroundColor(getColor(R.color.background));
        if (Build.VERSION.SDK_INT >= 19) {
            AndroidBug5497Workaround.assistActivity(this, true);
        }
        vButton = findViewById(R.id.ll_opt);
        mBtnUnFinish = (Button) findViewById(R.id.btn_un_finish);
        mBtnOpt = (Button) findViewById(R.id.btn_opt);
        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        tvSpinnerLabel = (TextView) findViewById(R.id.tv_spinner_label);
        evSpinner = (EditText) findViewById(R.id.ev_spinner);
        ivSpinnerAdd = (ImageView) findViewById(R.id.iv_spinner_add);
        ivSpinnerSub = (ImageView) findViewById(R.id.iv_spinner_sub);
        mItemRequestTime = (WorkListDetailItemView) findViewById(R.id.div_request_time);
        mItemName = (WorkListDetailItemView) findViewById(R.id.div_name);
        mItemAddress = (WorkListDetailItemView) findViewById(R.id.div_address);
        mItemGoods = (WorkListDetailItemView) findViewById(R.id.div_goods);
        mItemType = (WorkListDetailItemView) findViewById(R.id.div_type);
        mItemNum = (WorkListDetailItemView) findViewById(R.id.div_num);
        mItemReport = (WorkListDetailItemView) findViewById(R.id.div_report);
        mItemRequestTime.setLabel("要求时间：");
        mItemName.setLabel("姓名：");
        tvMobile = (TextView) findViewById(R.id.tv_phone);
        ivMobile = (ImageView) findViewById(R.id.iv_phone);
        mItemAddress.setLabel("地址：");
        mItemGoods.setLabel("产品：");
        mItemType.setLabel("类型：");
        mItemNum.setLabel("数量：");
        workListId = getIntent().getLongExtra("WorkListId", -1);
        type = getIntent().getIntExtra("type", WorkListAdapter.TYPE_TODO);
        mItemReport.getTvLabel().setVisibility(View.GONE);
        if (isFinishWorkList()) {
            evSpinner.setBackgroundColor(Color.TRANSPARENT);
            ivSpinnerAdd.setVisibility(View.GONE);
            ivSpinnerSub.setVisibility(View.GONE);
            vButton.setVisibility(View.GONE);
            findViewById(R.id.fl_status).setVisibility(View.GONE);
            mItemReport.getTvContent().setTextColor(getColor(R.color.gray_777777));
        } else {
            mItemReport.getTvContent().setTextColor(Color.RED);
        }
        mStatusView.showLoadingView();
        mBtnOpt.setVisibility(View.GONE);
        mLoadingDialogHelper = new LoadingDialogHelper(this);
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mBtnOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFinishWorkList() && mInfo.hasBtn()) {
                    showCancelDialog();
                }
            }
        });
        ivSpinnerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfo != null) {
                    mInfo.setMoneyOrCount2(mInfo.getMoneyOrCount2() + 1);
                    evSpinner.setText(mInfo.getMoneyOrCount2() + "");
                }
            }
        });
        ivSpinnerSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfo != null) {
                    if (mInfo.getMoneyOrCount2() - 1 > 0) {
                        mInfo.setMoneyOrCount2(mInfo.getMoneyOrCount2() - 1);
                        evSpinner.setText(mInfo.getMoneyOrCount2() + "");
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
                sendEmptyBackgroundMessageDelayed(MSG_BACK_LOAD, 300);
            }
        });
        mBtnUnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnFinishDialog();
            }
        });
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFinishDialog();
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
                loadDataTask();
                break;
            case MSG_BACK_UN_FINISH_WORK:
                unFinishWorkTask();
                break;
            case MSG_BACK_FINISH_WORK:
                finishWorkTask();
                break;
            case MSG_BACK_CANCEL:
                cancelTask();
                break;
        }
    }

    /**
     * 回退网络请求
     */
    private void cancelTask() {
        HttpRequestManager<FinishWorkResponse> requestWG = new HttpRequestManager<FinishWorkResponse>(
                URLConfig.DETAIL_CANCEL, FinishWorkResponse.class);
        requestWG.addParam("id", workListId + "");
        Message msgWG = obtainUiMessage();
        msgWG.what = MSG_UI_FINISH_WORK;
        msgWG.obj = requestWG.sendRequest();
        msgWG.sendToTarget();
    }

    /**
     * 完工确认数据请求
     */
    private void finishWorkTask() {
        HttpRequestManager<FinishWorkResponse> requestWG = new HttpRequestManager<FinishWorkResponse>(
                URLConfig.DETAIL_SENDGOODS, FinishWorkResponse.class);
        requestWG.addParam("id", workListId + "");
        if (evSpinner != null && !TextUtils.isEmpty(evSpinner.getText())) {
            requestWG.addParam("count", evSpinner.getText().toString());
        } else {
            if (mInfo != null) {
                requestWG.addParam("count", mInfo.getMoneyOrCount2() + "");
            }
        }
        if (mEvFinishReport != null) {
            requestWG.addParam("report", mEvFinishReport.getText().toString());
        }
        Message msgWG = obtainUiMessage();
        msgWG.what = MSG_UI_FINISH_WORK;
        msgWG.obj = requestWG.sendRequest();
        msgWG.sendToTarget();
    }

    /**
     * 未完工确认数据请求
     */
    private void unFinishWorkTask() {
        HttpRequestManager<FinishWorkResponse> requestQH = new HttpRequestManager<FinishWorkResponse>(
                URLConfig.UN_FINISH_WORK, FinishWorkResponse.class);
        requestQH.addParam("id", workListId + "");
        if (mEvUnFinishReport != null) {
            requestQH.addParam("report", mEvUnFinishReport.getText().toString());
        }
        Message msgQH = obtainUiMessage();
        msgQH.what = MSG_UI_UN_FINISH_WORK;
        msgQH.obj = requestQH.sendRequest();
        msgQH.sendToTarget();
    }

    /**
     * 加载数据的请求
     */
    private void loadDataTask() {
        HttpRequestManager<WorkListDetailResponse> requestLoad = new HttpRequestManager<WorkListDetailResponse>(
                URLConfig.WORK_LIST_DETAIL, WorkListDetailResponse.class);
        requestLoad.addParam("id", workListId + "");
        Message msgLoad = obtainUiMessage();
        msgLoad.what = MSG_UI_LOAD;
        msgLoad.obj = requestLoad.sendRequest();
        msgLoad.sendToTarget();
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case MSG_UI_LOAD:
                updateData(msg);
                break;
            case MSG_UI_FINISH_WORK:
                dealFinishWork(msg);
                break;
            case MSG_UI_UN_FINISH_WORK:
                dealUnFinishWork(msg);
                break;
            case MSG_UI_CANCEL:
                dealCancel(msg);
                break;
            case MSG_UI_FINISH:
                finish();
                break;
        }
    }

    /**
     * 处理回退返回
     *
     * @param msg
     */
    private void dealCancel(Message msg) {
        if (msg.obj != null && msg.obj instanceof FinishWorkResponse) {
            FinishWorkResponse response = (FinishWorkResponse) msg.obj;
            if (response.getCode() == FinishWorkResponse.CODE_SUCCESS) {
                switch (type) {
                    case WorkListAdapter.TYPE_TODAY:
                        sendBroadcast(BroadcastActions.ACTION_UPDATE_TODAY_WORK_LIST);
                        break;
                    case WorkListAdapter.TYPE_HISTORY:
                        sendBroadcast(BroadcastActions.ACTION_UPDATE_HISTORY_WORK_LIST);
                        break;
                }
                sendEmptyUiMessageDelayed(MSG_UI_FINISH, FINISH_DELAY_TIME);
            }
            mLoadingDialogHelper.hide();
            ToastUtil.show(response.getMsg());
        } else {
            ToastUtil.show(R.string.load_error);
        }
    }

    /**
     * 处理完工返回
     *
     * @param msg
     */
    private void dealFinishWork(Message msg) {
        if (msg.obj != null && msg.obj instanceof FinishWorkResponse) {
            FinishWorkResponse response = (FinishWorkResponse) msg.obj;
            if (response.getCode() == FinishWorkResponse.CODE_SUCCESS) {
                sendBroadcast(BroadcastActions.ACTION_UPDATE_TODO_WORK_LIST);
                sendEmptyUiMessageDelayed(MSG_UI_FINISH, FINISH_DELAY_TIME);
            }
            mLoadingDialogHelper.hide();
            ToastUtil.show(response.getMsg());
        } else {
            ToastUtil.show(R.string.load_error);
        }
    }

    /**
     * 未完工返回
     *
     * @param msg
     */
    private void dealUnFinishWork(Message msg) {
        if (msg.obj != null && msg.obj instanceof FinishWorkResponse) {
            FinishWorkResponse response = (FinishWorkResponse) msg.obj;
            if (response == null) {
                return;
            }
            if (response.getCode() == FinishWorkResponse.CODE_SUCCESS) {
                sendBroadcast(BroadcastActions.ACTION_UPDATE_TODO_WORK_LIST);
            }
            if (mItemReport != null && response != null) {
                mItemReport.setVisibility(View.VISIBLE);
                mItemReport.setContent(response.getMsg());
            }
            mLoadingDialogHelper.hide();
        } else {
            ToastUtil.show(R.string.load_error);
        }
    }

    /**
     * 更新ui数据显示
     *
     * @param msg
     */
    private void updateData(Message msg) {
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
                if (!TextUtils.isEmpty(mInfo.getReport())) {
                    mItemReport.setVisibility(View.VISIBLE);
                    mItemReport.setContent(mInfo.getReport());
                } else {
                    mItemReport.setVisibility(View.GONE);
                }
                int count = 0;
                if (mInfo != null) {
                    count = mInfo.getMoneyOrCount2();
                }
                if (TextUtils.equals(mInfo.getType(), WorkListInfo.TYPE_TAOCAN)
                        || TextUtils.equals(mInfo.getType(), WorkListInfo.TYPE_XIAOTUI)) {
                    mItemNum.setLabel("金额：");
                    mItemNum.setContent(mInfo.getMoneyOrCount() + "元");
                    tvSpinnerLabel.setText("本次收款：");
                    if (isFinishWorkList()) {
                        evSpinner.setText("" + count + "元");
                    } else {
                        evSpinner.setText("" + count);
                    }
                } else if (TextUtils.equals(mInfo.getType(), WorkListInfo.TYPE_PEISONG)) {
                    mItemNum.setLabel("数量：");
                    mItemNum.setContent(mInfo.getMoneyOrCount() + "桶");
                    tvSpinnerLabel.setText("回收空桶：");
                    if (isFinishWorkList()) {
                        evSpinner.setText("" + count + "桶");
                    } else {
                        evSpinner.setText("" + count);
                    }
                }
                if (isFinishWorkList() && mInfo.hasBtn()) {
                    mBtnOpt.setVisibility(View.VISIBLE);
                    mBtnOpt.setText(mInfo.getBtnName());
                }
            }
        } else {
            mStatusView.showFailView();
        }
    }

    /**
     * 判断是否为历史工单
     *
     * @return
     */
    private boolean isFinishWorkList() {
        return type != WorkListAdapter.TYPE_TODO;
    }

    /**
     * 完工确认弹窗
     */
    private void showFinishDialog() {
        if (!isFinishing()) {
            if (mFinishDialog == null) {
                mFinishDialog = new BaseDialog(this);
                mFinishDialog.setWindow(R.style.alpha_animation, 0.0f);
                mFinishDialog.setContentView(R.layout.dialog_finish);
                mEvFinishReport = (EditText) mFinishDialog.findViewById(R.id.ev_report);
                mFinishDialog.findViewById(R.id.btn_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mFinishDialog != null) {
                                    mFinishDialog.dismiss();
                                }
                            }
                        });
                mFinishDialog.findViewById(R.id.btn_ok)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mFinishDialog != null) {
                                    mFinishDialog.dismiss();
                                }
                                mLoadingDialogHelper.show();
                                sendEmptyBackgroundMessage(MSG_BACK_FINISH_WORK);
                            }
                        });
            }
            mEvFinishReport.setText("");
            mFinishDialog.show();
        }
    }

    /**
     * 回退弹窗
     */
    private void showCancelDialog() {
        if (!isFinishing()) {
            if (mCancelDialog == null) {
                mCancelDialog = new BaseDialog(this);
                mCancelDialog.setWindow(R.style.alpha_animation, 0.0f);
                mCancelDialog.setContentView(R.layout.dialog_loading);
                mTvCancelTitle = (TextView) mCancelDialog.findViewById(R.id.tv_title);
                mCancelDialog.findViewById(R.id.btn_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mCancelDialog != null) {
                                    mCancelDialog.dismiss();
                                }
                            }
                        });
                mCancelDialog.findViewById(R.id.btn_ok)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mCancelDialog != null) {
                                    mCancelDialog.dismiss();
                                }
                                sendEmptyBackgroundMessage(MSG_BACK_CANCEL);
                                mLoadingDialogHelper.show();
                            }
                        });
            }
            if (!TextUtils.isEmpty(mInfo.getConfirmInfo())) {
                mTvCancelTitle.setText(mInfo.getConfirmInfo());
            }
            mLoadingDialogHelper.show();
        }
    }

    /**
     * 未完工确认弹窗
     */
    private void showUnFinishDialog() {
        if (!isFinishing()) {
            if (mUnFinishDialog == null) {
                mUnFinishDialog = new BaseDialog(this);
                mUnFinishDialog.setWindow(R.style.alpha_animation, 0.0f);
                mUnFinishDialog.setContentView(R.layout.dialog_un_finish);
                mEvUnFinishReport = (EditText) mUnFinishDialog.findViewById(R.id.ev_report);
                mUnFinishDialog.findViewById(R.id.btn_cancel)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mUnFinishDialog != null) {
                                    mUnFinishDialog.dismiss();
                                }
                            }
                        });
                mUnFinishDialog.findViewById(R.id.btn_ok)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mUnFinishDialog != null) {
                                    mUnFinishDialog.dismiss();
                                }
                                mLoadingDialogHelper.show();
                                sendEmptyBackgroundMessage(MSG_BACK_UN_FINISH_WORK);
                            }
                        });
            }
            mEvUnFinishReport.setText("");
            mUnFinishDialog.show();
        }
    }
}
