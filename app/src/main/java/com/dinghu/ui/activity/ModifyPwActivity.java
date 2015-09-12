
package com.dinghu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;

import java.util.List;

/**
 * 修改密码页面
 */
public class ModifyPwActivity extends CommonTitleActivity implements TextWatcher {
    private EditText mEvPwOld;
    private EditText mEvPwNew;
    private EditText mEvPwAgain;
    private Button mBtnOk;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_modify_pw);
        setTitle(R.string.title_modify_pw);
        // 设置点击页面其他地方隐藏软键盘
        setHideInputView(R.id.root);
    }

    @Override
    protected void initEvent() {
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            finish();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(mEvPwOld.getText()) || TextUtils.isEmpty(mEvPwNew.getText()) || TextUtils.isEmpty(mEvPwAgain.getText())) {
            mBtnOk.setEnabled(false);
        } else {
            mBtnOk.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
