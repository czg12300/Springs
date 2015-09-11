
package com.dinghu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;

import java.util.List;

/**
 * 登录页面
 */
public class LoginActivity extends CommonTitleActivity
        implements TextWatcher, View.OnClickListener {
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
        goActivity(MainActivity.class);
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
            goActivity(MainActivity.class);
            finish();
        } else if (v.getId() == R.id.tv_forget_pw) {
            // TODO 忘记密码
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
