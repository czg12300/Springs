package com.dinghu.ui.helper;

import com.dinghu.R;
import com.dinghu.utils.Utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import cn.common.ui.BaseDialog;

import java.util.Calendar;

/**
 * 描述:处理时间选择的相关逻辑
 *
 * @author jakechen
 * @since 2015/11/9 15:35
 */
public class DateSelectorHelper {
  private static final String START = "start";

  private static final String END = "end";
  private final TextView mTvStart;
  private final TextView mTvEnd;
  private Activity mActivity;
  private BaseDialog mDateSelectDialog;

  private DatePicker mDatePicker;
  private Button mBtnOk;
  private TextView mTvTitle;
  private String mStartDate;
  private String mEndDate;
  private IListener mIListener;
  private View mContentView;


  public DateSelectorHelper(Activity activity) {
    mActivity = activity;
    mContentView = mActivity.getLayoutInflater().inflate(R.layout.header_list_history, null);
    mTvStart = (TextView) mContentView.findViewById(R.id.tv_date_start);
    mTvEnd = (TextView) mContentView.findViewById(R.id.tv_date_end);
    mContentView.findViewById(R.id.fl_start).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showDateSelectDialog(START);
      }
    });
    mContentView.findViewById(R.id.fl_end).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showDateSelectDialog(END);
      }
    });
    initData();

  }

  private void initData() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(System.currentTimeMillis());
    if (cal.get(Calendar.DAY_OF_MONTH) > 1) {
      mStartDate = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-1";
    } else {
      mStartDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) - 1) + "-1";
    }
    mEndDate = Utils.getCurrentTime();
    updateStartTime();
    updateEndTime();
  }


  private void showDateSelectDialog(String tag) {
    if (mActivity != null && !mActivity.isFinishing()) {
      if (mDateSelectDialog == null) {
        mDateSelectDialog = new BaseDialog(mActivity);
        mDateSelectDialog.setWindow(R.style.alpha_animation, 0.3f);
        mDateSelectDialog.setContentView(R.layout.dialog_select_date);
        mDatePicker = (DatePicker) mDateSelectDialog.findViewById(R.id.date_picker);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        mBtnOk = (Button) mDateSelectDialog.findViewById(R.id.btn_ok);
        mTvTitle = (TextView) mDateSelectDialog.findViewById(R.id.tv_title);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (mDatePicker != null) {
              String type = (String) v.getTag();
              if (TextUtils.equals(type, START)) {
                mStartDate = mDatePicker.getYear() + "-" + (mDatePicker.getMonth() + 1) + "-" + mDatePicker.getDayOfMonth();
                updateStartTime();
              } else if (TextUtils.equals(type, END)) {
                mEndDate = mDatePicker.getYear() + "-" + (mDatePicker.getMonth() + 1) + "-" + mDatePicker.getDayOfMonth();
                updateEndTime();
              }
            }
            if (mDateSelectDialog != null) {
              mDateSelectDialog.dismiss();
            }
            if (mIListener != null) {
              mIListener.loadData();
            }
          }
        });
      }
      if (TextUtils.equals(tag, START)) {
        int[] ints = getTime(mStartDate);
        mTvTitle.setText("请选择起初日期");
        mDatePicker.updateDate(ints[0], ints[1] - 1, ints[2]);
      } else if (TextUtils.equals(tag, END)) {
        mTvTitle.setText("请选择结束日期");
        int[] ints = getTime(mEndDate);
        mDatePicker.updateDate(ints[0], ints[1] - 1, ints[2]);
      }
      mBtnOk.setTag(tag);
      mDateSelectDialog.show();
    }
  }

  private void updateStartTime() {
    int[] ints = getTime(mStartDate);
    if (mTvStart != null) {
      mTvStart.setText(ints[0] + "年" + ints[1] + "月" + ints[2] + "日");
    }
  }

  private void updateEndTime() {
    int[] ints = getTime(mEndDate);
    if (mTvEnd != null) {
      mTvEnd.setText(ints[0] + "年" + ints[1] + "月" + ints[2] + "日");
    }
  }

  private int[] getTime(String time) {
    time = time.trim();
    int[] is = new int[3];
    String[] ss = time.split("-");
    for (int i = 0; i < 3; i++) {
      is[i] = Integer.valueOf(ss[i]);
    }
    return is;
  }


  public void setListener(IListener listener) {
    mIListener = listener;
  }

  public View getView() {
    return mContentView;
  }

  public String getStartDate() {
    return mStartDate;
  }

  public String getEndDate() {
    return mEndDate;
  }

  public static interface IListener {

    void loadData();
  }
}
