
package com.dinghu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.http.response.UserResponse;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.utils.MD5Util;
import com.dinghu.utils.ToastUtil;

import java.util.List;

/**
 * 登录页面
 */
public class LoginActivity extends CommonTitleActivity
        implements TextWatcher, View.OnClickListener {
    private static final int MSG_BACK_LOGIN = 0;
    private static final int MSG_UI_LOGIN = 1;
    private EditText mEvMobile;

    private EditText mEvPw;

    private Button mBtnOk;

    private TextView mTvForgetPw;

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
            sendEmptyBackgroundMessage(MSG_BACK_LOGIN);
        } else if (v.getId() == R.id.tv_forget_pw) {
            //  忘记密码
        }
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        if (msg.what == MSG_BACK_LOGIN) {
            HttpRequestManager<UserResponse> request = new HttpRequestManager<UserResponse>(URLConfig.LOGIN, UserResponse.class);
            String mobile = mEvMobile.getText().toString();
            InitShareData.setMobile(mobile);
            request.addParam("tel", mobile);
            request.addParam("pwd", MD5Util.md5(mEvPw.getText().toString()));
            Message message = obtainUiMessage();
            message.what = MSG_UI_LOGIN;
            message.obj = request.sendRequest();
            message.sendToTarget();
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
                            finish();
                            break;
                        case UserResponse.CODE_SUCCESS_MPW:
                            InitShareData.setUserId(info.getUserId());
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isLoginJump", true);
                            goActivity(ModifyPwActivity.class, bundle);
                            finish();
                            break;
                    }
                    ToastUtil.show(info.getMsg());
                }
            } else {
                ToastUtil.show("网络异常");
            }
        }
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
