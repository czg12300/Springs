
package com.dinghu.ui.activity;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import java.util.List;

/**
 * 登录页面
 */
public class MyAccountBookActivity extends CommonTitleActivity
        implements TextWatcher, View.OnClickListener {

    @Override
    protected void initView() {
        mVTitle.setVisibility(View.GONE);
        setSwipeBackEnable(false);
        setContentView(R.layout.activity_login);
        // 设置点击页面其他地方隐藏软键盘
        setHideInputView(R.id.root);
        goActivity(MainActivity.class);
    }

    @Override
    protected void initEvent() {
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
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
