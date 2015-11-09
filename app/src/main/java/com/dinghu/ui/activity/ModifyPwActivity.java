
package com.dinghu.ui.activity;

import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dinghu.R;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.ModifyPwResponse;
import com.dinghu.ui.helper.LoadingDialogHelper;
import com.dinghu.utils.MD5Util;
import com.dinghu.utils.ToastUtil;

/**
 * 修改密码页面
 */
public class ModifyPwActivity extends CommonTitleActivity implements TextWatcher {
    private static final int MSG_BACK_MODIFY_PW = 0;

    private static final int MSG_UI_MODIFY_PW = 1;

    private EditText mEvPwOld;

    private EditText mEvPwNew;

    private EditText mEvPwAgain;

    private Button mBtnOk;

    private LoadingDialogHelper mLoadingDialogHelper;

    @Override
    protected void initView() {
        if (getIntent().getBooleanExtra("isLoginJump", false)) {
            setSwipeBackEnable(false);
            mIvBack.setVisibility(View.GONE);
        }
        setContentView(R.layout.activity_modify_pw);
        setTitle(R.string.title_modify_pw);
        // 设置点击页面其他地方隐藏软键盘
        setHideInputView(R.id.root);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mEvPwOld = (EditText) findViewById(R.id.ev_pw_old);
        mEvPwNew = (EditText) findViewById(R.id.ev_pw_new);
        mEvPwAgain = (EditText) findViewById(R.id.ev_pw_again);
        mEvPwOld.addTextChangedListener(this);
        mEvPwNew.addTextChangedListener(this);
        mEvPwAgain.addTextChangedListener(this);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialogHelper.show();
                sendEmptyBackgroundMessageDelayed(MSG_BACK_MODIFY_PW, 300);
            }
        });
        mLoadingDialogHelper = new LoadingDialogHelper(this);
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        if (msg.what == MSG_BACK_MODIFY_PW) {
            HttpRequestManager<ModifyPwResponse> request = new HttpRequestManager<ModifyPwResponse>(
                    URLConfig.MODIFY_PW, ModifyPwResponse.class);
            request.addParam("id", "" + InitShareData.getUserId());
            request.addParam("tel", "" + InitShareData.getMobile());
            request.addParam("oldPwd", MD5Util.md5(mEvPwOld.getText().toString()));
            request.addParam("newPwd1", MD5Util.md5(mEvPwNew.getText().toString()));
            request.addParam("newPwd2", MD5Util.md5(mEvPwAgain.getText().toString()));
            Message message = obtainUiMessage();
            message.what = MSG_UI_MODIFY_PW;
            message.obj = request.sendRequest();
            message.sendToTarget();
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        if (msg.what == MSG_UI_MODIFY_PW) {
            mLoadingDialogHelper.hide();
            if (msg.obj != null && msg.obj instanceof ModifyPwResponse) {
                ModifyPwResponse info = (ModifyPwResponse) msg.obj;
                if (info != null) {
                    switch (info.getCode()) {
                        case ModifyPwResponse.CODE_FAIL:
                            break;
                        case ModifyPwResponse.CODE_SUCCESS:
                            if (getIntent().getBooleanExtra("isLoginJump", false)) {
                                goActivity(MainActivity.class);
                            }
                            finish();
                            break;
                    }
                    ToastUtil.show(info.getMsg());
                }
            } else {
                ToastUtil.show(R.string.load_error);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(mEvPwOld.getText()) || TextUtils.isEmpty(mEvPwNew.getText())
                || TextUtils.isEmpty(mEvPwAgain.getText())) {
            mBtnOk.setEnabled(false);
        } else {
            mBtnOk.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
