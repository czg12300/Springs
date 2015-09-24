package com.dinghu.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dinghu.R;

import java.util.Calendar;

import cn.common.ui.BaseDialog;

/**
 * 登录页面
 */
public class MyAccountBookActivity extends CommonTitleActivity implements View.OnClickListener {
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
    mTvDate = (TextView) findViewById(R.id.tv_date);
  }

  @Override
  protected void initEvent() {
    findViewById(R.id.fl_date).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.fl_date) {
      showDateSelectDialog();
    }
  }

  private void showDateSelectDialog() {
    if (!isFinishing()) {
      if (mDateSelectDialog == null) {
        mDateSelectDialog = new BaseDialog(this);
        mDateSelectDialog.setWindow(R.style.alpha_animation, 0.3f);
        mDateSelectDialog.setContentView(R.layout.dialog_select_birthday);
        mDatePicker = (DatePicker) mDateSelectDialog.findViewById(R.id.date_picker);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        mTvTitle = (TextView) mDateSelectDialog.findViewById(R.id.tv_title);
        mDateSelectDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (mDatePicker != null) {
              mTvDate.setText(mDatePicker.getYear() + "年" + (mDatePicker.getMonth() + 1) + "月");
            }
            if (mDateSelectDialog != null) {
              mDateSelectDialog.dismiss();
            }
          }
        });
      }
      mDateSelectDialog.show();
    }
  }


}
