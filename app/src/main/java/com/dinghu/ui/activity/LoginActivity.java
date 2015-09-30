
package com.dinghu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dinghu.MessageService;
import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.StoreInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.StoreInfoResponse;
import com.dinghu.logic.http.response.UserResponse;
import com.dinghu.ui.adapter.StoreListAdapter;
import com.dinghu.utils.MD5Util;
import com.dinghu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.common.ui.BaseDialog;

/**
 * 登录页面
 */
public class LoginActivity extends CommonTitleActivity
        implements TextWatcher, View.OnClickListener {
    private static final int MSG_BACK_LOGIN = 0;

    private static final int MSG_BACK_LOAD_STORES_LIST = 1;

    private static final int MSG_UI_LOGIN = 1;

    private static final int MSG_UI_LOAD_STORES_LIST = 2;

    private EditText mEvMobile;

    private EditText mEvPw;

    private Button mBtnOk;

    private TextView mTvForgetPw;

    private BaseDialog mStoreDialog;

    private StoreListAdapter mStoreListAdapter;

    private String storeId;

    @Override
    protected void initView() {
        mVTitle.setVisibility(View.GONE);
        setSwipeBackEnable(false);
        setContentView(R.layout.activity_login);
        mEvMobile = (EditText) findViewById(R.id.ev_account);
        mEvPw = (EditText) findViewById(R.id.ev_pw);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mTvForgetPw = (TextView) findViewById(R.id.tv_forget_pw);
        // 设置点击页面其他地方隐藏软键盘
        setHideInputView(R.id.root);
    }

    @Override
    protected void initEvent() {
        mEvPw.addTextChangedListener(this);
        mEvMobile.addTextChangedListener(this);
        mBtnOk.setEnabled(false);
        mBtnOk.setOnClickListener(this);
        mTvForgetPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            mBtnOk.setText("登录中...");
            mBtnOk.setEnabled(false);
            sendEmptyBackgroundMessage(MSG_BACK_LOAD_STORES_LIST);
        } else if (v.getId() == R.id.tv_forget_pw) {
            // 忘记密码
        }
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        String mobile = mEvMobile.getText().toString();
        switch (msg.what) {
            case MSG_BACK_LOAD_STORES_LIST:
                HttpRequestManager<StoreInfoResponse> requestStores = new HttpRequestManager<StoreInfoResponse>(
                        URLConfig.GET_STORE_LIST, StoreInfoResponse.class);
                InitShareData.setMobile(mobile);
                requestStores.addParam("tel", mobile);
                Message msgStore = obtainUiMessage();
                msgStore.what = MSG_UI_LOAD_STORES_LIST;
                msgStore.obj = requestStores.sendRequest();
                msgStore.sendToTarget();
                break;
            case MSG_BACK_LOGIN:
                long id = (long) msg.obj;
                HttpRequestManager<UserResponse> request = new HttpRequestManager<UserResponse>(
                        URLConfig.LOGIN, UserResponse.class);
                InitShareData.setMobile(mobile);
                request.addParam("tel", mobile);
                request.addParam("pwd", MD5Util.md5(mEvPw.getText().toString()));
                request.addParam("staId", "" + id);
                Message message = new Message();
                message.what = MSG_UI_LOGIN;
                message.obj = request.sendRequest();
                sendUiMessageDelayed(message, 500);
                break;
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        if (msg.what == MSG_UI_LOGIN) {
            if (msg.obj != null && msg.obj instanceof UserResponse) {
                UserResponse info = (UserResponse) msg.obj;
                if (info != null) {
                    switch (info.getCode()) {
                        case UserResponse.CODE_FAIL:
                            break;
                        case UserResponse.CODE_SUCCESS:
                            InitShareData.setUserId(info.getUserId());
                            goActivity(MainActivity.class);
                            startService(new Intent(this, MessageService.class));
                            finish();
                            break;
                        case UserResponse.CODE_SUCCESS_MPW:
                            InitShareData.setUserId(info.getUserId());
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isLoginJump", true);
                            goActivity(ModifyPwActivity.class, bundle);
                            startService(new Intent(this, MessageService.class));
                            finish();
                            break;
                    }
                    ToastUtil.show(info.getMsg());
                }
            } else {
                ToastUtil.show(R.string.load_error);
            }
            mBtnOk.setText(R.string.login_now);
            mBtnOk.setEnabled(true);
        } else if (msg.what == MSG_UI_LOAD_STORES_LIST) {
            if (msg.obj != null && msg.obj instanceof StoreInfoResponse) {
                StoreInfoResponse info = (StoreInfoResponse) msg.obj;
                if (info != null) {
                    if (info.getCode() == StoreInfoResponse.CODE_SUCCESS
                            && info.getStoresInfoList() != null
                            && info.getStoresInfoList().size() > 0) {
                        showSelectStoreDialog(info.getStoresInfoList());
                    } else {
                        ToastUtil.show(info.getMsg());
                        mBtnOk.setEnabled(true);
                        mBtnOk.setText(R.string.login_now);
                    }
                }
            } else {
                mBtnOk.setEnabled(true);
                mBtnOk.setText(R.string.login_now);
                ToastUtil.show(R.string.load_error);
            }
        }
    }

    private void showSelectStoreDialog(ArrayList<StoreInfo> list) {
        if (isFinishing() || list == null || list.size() < 1) {
            return;
        }
        if (mStoreDialog == null) {
            mStoreDialog = new BaseDialog(this);
            mStoreDialog.setWindow(R.style.alpha_animation, 0.3f);
            mStoreDialog.setContentView(R.layout.dialog_select_store);
            mStoreDialog.setCanceledOnTouchOutside(false);
            ListView lv = (ListView) mStoreDialog.findViewById(R.id.lv_list);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StoreInfo info = (StoreInfo) parent.getAdapter().getItem(position);
                    if (info != null) {
                        Message message = obtainBackgroundMessage();
                        message.what = MSG_BACK_LOGIN;
                        message.obj = info.getId();
                        message.sendToTarget();
                    } else {
                        sendEmptyUiMessage(MSG_UI_LOGIN);
                    }
                    if (mStoreDialog != null) {
                        mStoreDialog.dismiss();
                    }
                }
            });
            mStoreListAdapter = new StoreListAdapter(this);
            lv.setAdapter(mStoreListAdapter);
        }
        mStoreListAdapter.setData(list);
        mStoreDialog.show();
    }

    @Override
    public void setupBroadcastActions(List<String> actions) {
        super.setupBroadcastActions(actions);
        actions.add(BroadcastActions.ACTION_FINISH_ACITIVTY_BEFORE_MAIN);
    }

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        super.handleBroadcast(context, intent);
        String action = intent.getAction();
        if (TextUtils.equals(action, BroadcastActions.ACTION_FINISH_ACITIVTY_BEFORE_MAIN)) {
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(mEvPw.getText()) || TextUtils.isEmpty(mEvMobile.getText())) {
            mBtnOk.setEnabled(false);
        } else {
            mBtnOk.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
