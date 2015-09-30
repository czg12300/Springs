
package com.dinghu.ui.activity;

import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.AccountResponse;
import com.dinghu.ui.widget.StatusView;
import com.dinghu.utils.Utils;

import java.util.Calendar;

import cn.common.ui.BaseDialog;

/**
 * 登录页面
 */
public class MyAccountBookActivity extends CommonTitleActivity {
    private static final int MSG_BACK_LOAD = 0;

    private static final int MSG_UI_LOAD = 1;

    private TextView mTvWorkListCount;

    private TextView mTvSendCount;

    private TextView mTvNullCount;

    private TextView mTvLongCount;

    private TextView mTvHeightCount;

    private TextView mTvWorkListCount1;

    private TextView mTvReceiveCount;

    private TextView mTvPayCount;

    private TextView mTvDate;

    private BaseDialog mDateSelectDialog;

    private DatePicker mDatePicker;

    private StatusView mStatusView;

    private String mDateMonth;

    @Override
    protected void initView() {
        setTitle(R.string.title_my_account_book);
        setContentView(R.layout.activity_my_account);
        mStatusView = (StatusView) findViewById(R.id.status_view);
        mStatusView.setContentView(R.layout.view_accont_book_content);
        mTvWorkListCount = (TextView) findViewById(R.id.tv_work_list_count);
        mTvSendCount = (TextView) findViewById(R.id.tv_send_count);
        mTvNullCount = (TextView) findViewById(R.id.tv_null_count);
        mTvLongCount = (TextView) findViewById(R.id.tv_long_count);
        mTvHeightCount = (TextView) findViewById(R.id.tv_height_count);
        mTvWorkListCount1 = (TextView) findViewById(R.id.tv_work_list_count2);
        mTvReceiveCount = (TextView) findViewById(R.id.tv_receive_count);
        mTvPayCount = (TextView) findViewById(R.id.tv_pay_count);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mStatusView.showLoadingView();
        int[] ints = getTime(Utils.getCurrentTime());
        mDateMonth = ints[0] + "-" + ints[1];
        mTvDate.setText(ints[0] + "年" + ints[1] + "月");
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    private int[] getTime(String time) {
        time = time.trim();
        int[] is = new int[2];
        String[] ss = time.split("-");
        for (int i = 0; i < 2; i++) {
            is[i] = Integer.valueOf(ss[i]);
        }
        return is;
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        if (msg.what == MSG_BACK_LOAD) {
            HttpRequestManager<AccountResponse> requestManager = new HttpRequestManager<AccountResponse>(
                    URLConfig.ACCOUNT_BOOK, AccountResponse.class);
            requestManager.addParam("id", InitShareData.getUserId() + "");
            requestManager.addParam("date", mDateMonth);
            Message message = obtainUiMessage();
            message.obj = requestManager.sendRequest();
            message.what = MSG_UI_LOAD;
            message.sendToTarget();
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        if (msg.what == MSG_UI_LOAD) {
            if (msg.obj != null) {
                mStatusView.showContentView();
                AccountResponse response = (AccountResponse) msg.obj;
                if (response != null && response.isOk()) {
                    mTvWorkListCount.setText("" + response.getPs_formTotal());
                    mTvSendCount.setText("" + response.getPs_ssCount());
                    mTvNullCount.setText("" + response.getPs_emptyCount());
                    mTvLongCount.setText("" + response.getPs_ct());
                    mTvHeightCount.setText("" + response.getPs_gc());
                    mTvWorkListCount1.setText("" + response.getTc_formTotal());
                    mTvReceiveCount.setText("" + response.getTc_skAmount());
                    mTvPayCount.setText("" + response.getTc_jkAmount());
                } else {
                    mStatusView.showFailView();
                }
            } else {
                mStatusView.showFailView();
            }

        }
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.fl_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateSelectDialog();
            }
        });
        mStatusView.setStatusListener(new StatusView.StatusListener() {
            @Override
            public void onLoad() {
                sendEmptyBackgroundMessage(MSG_BACK_LOAD);
            }
        });
    }

    private void showDateSelectDialog() {
        if (!isFinishing()) {
            if (mDateSelectDialog == null) {
                mDateSelectDialog = new BaseDialog(this);
                mDateSelectDialog.setWindow(R.style.alpha_animation, 0.3f);
                mDateSelectDialog.setContentView(R.layout.dialog_select_date);
                mDatePicker = (DatePicker) mDateSelectDialog.findViewById(R.id.date_picker);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH), null);
                ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0)).getChildAt(2)
                        .setVisibility(View.GONE);
                mTvTitle = (TextView) mDateSelectDialog.findViewById(R.id.tv_title);
                mDateSelectDialog.findViewById(R.id.btn_ok)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mDatePicker != null) {
                                    mDateMonth = mDatePicker.getYear() + "-"
                                            + (mDatePicker.getMonth() + 1);
                                    mTvDate.setText(mDatePicker.getYear() + "年"
                                            + (mDatePicker.getMonth() + 1) + "月");
                                    sendEmptyBackgroundMessage(MSG_BACK_LOAD);
                                }
                                if (mDateSelectDialog != null) {
                                    mDateSelectDialog.dismiss();
                                }
                            }
                        });
            }
            int[] ints = getTime(mDateMonth);
            mDatePicker.updateDate(ints[0], ints[1] - 1, 1);
            mDateSelectDialog.show();
        }
    }

}
