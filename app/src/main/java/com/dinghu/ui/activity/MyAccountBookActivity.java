
package com.dinghu.ui.activity;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * 登录页面
 */
public class MyAccountBookActivity extends CommonTitleActivity
        implements View.OnClickListener {
    private TextView mTvWorkListCount;
    private TextView mTvSendCount;
    private TextView mTvNullCount;
    private TextView mTvLongCount;
    private TextView mTvHeightCount;
    private TextView mTvWorkListCount1;
    private TextView mTvReceiveCount;
    private TextView mTvPayCount;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_account);
        setTitle(R.string.title_my_account_book);
        mTvWorkListCount = (TextView) findViewById(R.id.tv_work_list_count);
        mTvSendCount = (TextView) findViewById(R.id.tv_send_count);
        mTvNullCount = (TextView) findViewById(R.id.tv_null_count);
        mTvLongCount = (TextView) findViewById(R.id.tv_long_count);
        mTvHeightCount = (TextView) findViewById(R.id.tv_height_count);
        mTvWorkListCount1 = (TextView) findViewById(R.id.tv_work_list_count2);
        mTvReceiveCount = (TextView) findViewById(R.id.tv_receive_count);
        mTvPayCount = (TextView) findViewById(R.id.tv_pay_count);
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.fl_date).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fl_date) {
            finish();
        }
    }

}
