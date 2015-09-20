
package com.dinghu.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.widget.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.common.ui.BaseDialog;
import cn.common.ui.adapter.BaseListAdapter;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public class HistoryListFragment extends BaseListFragment<WorkListInfo> {
    public static HistoryListFragment newInstance() {
        return new HistoryListFragment();
    }

    private static final String START = "start";
    private static final String END = "end";
    private BaseDialog mDateSelectDialog;
    private DatePicker mDatePicker;
    private Button mBtnOk;
    private TextView mTvStart;
    private TextView mTvEnd;
    private TextView mTvTitle;
    private String mStartDate;
    private String mEndDate;


    @Override
    protected List<WorkListInfo> loadData() {
        HttpRequestManager<WorkListResponse> request = new HttpRequestManager<WorkListResponse>(URLConfig.WORK_LIST_HISTORY, WorkListResponse.class);
        request.addParam("pageNum", getPageIndex() + "");
        request.addParam("pageSize", getPageSize() + "");
        request.addParam("employId", InitShareData.getUserId() + "");
        request.addParam("startDate", mStartDate);
        request.addParam("endDate", mEndDate);
        WorkListResponse response = request.sendRequest();
        if (response != null) {
            return response.getList();
        }
        return null;
    }

    @Override
    public void setContentView(View view) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        View header = inflate(R.layout.header_list_history);
        layout.addView(header);
        layout.addView(view, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        mTvStart = (TextView) header.findViewById(R.id.tv_date_start);
        mTvEnd = (TextView) header.findViewById(R.id.tv_date_end);
        header.findViewById(R.id.fl_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateSelectDialog(START);
            }
        });
        header.findViewById(R.id.fl_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateSelectDialog(END);
            }
        });
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        if (cal.get(Calendar.DAY_OF_MONTH) > 1) {
            mStartDate = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-1";
        } else {
            mStartDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) - 1) + "-1";
        }
        mEndDate = getCurrentTime();
        updateStartTime();
        updateEndTime();
        super.setContentView(layout);
    }

    @Override
    protected void addFooter(XListView mLvList) {
    }

    @Override
    protected void addHeader(XListView mLvList) {
    }

    @Override
    protected BaseListAdapter<WorkListInfo> createAdapter() {
        return new WorkListAdapter(getActivity());
    }

    private void showDateSelectDialog(String tag) {
        if (canShowDialog()) {
            if (mDateSelectDialog == null) {
                mDateSelectDialog = new BaseDialog(getActivity());
                mDateSelectDialog.setWindow(R.style.alpha_animation, 0.3f);
                mDateSelectDialog.setContentView(R.layout.dialog_select_birthday);
                mDatePicker = (DatePicker) mDateSelectDialog.findViewById(R.id.date_picker);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH), null);
                mBtnOk = (Button) mDateSelectDialog.findViewById(R.id.btn_ok);
                mTvTitle = (TextView) mDateSelectDialog.findViewById(R.id.tv_title);
                mBtnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDatePicker != null) {
                            String type = (String) v.getTag();
                            if (TextUtils.equals(type, START)) {
                                mStartDate = mDatePicker.getYear() + "-" + (
                                        mDatePicker.getMonth() + 1) + "-" + mDatePicker.getDayOfMonth();
                                updateStartTime();
                            } else if (TextUtils.equals(type, END)) {
                                mEndDate = mDatePicker.getYear() + "-" + (
                                        mDatePicker.getMonth() + 1) + "-" + mDatePicker.getDayOfMonth();
                                updateEndTime();
                            }
                            onRefresh();
                        }
                        if (mDateSelectDialog != null) {
                            mDateSelectDialog.dismiss();
                        }
                    }
                });
            }
            if (TextUtils.equals(tag, START)) {
                int[] ints = getTime(mStartDate);
                mTvTitle.setText("请选择起初日期");
                mDatePicker.init(ints[0], ints[1], ints[2], null);
            } else if (TextUtils.equals(tag, END)) {
                mTvTitle.setText("请选择结束日期");
                int[] ints = getTime(mEndDate);
                mDatePicker.init(ints[0], ints[1], ints[2], null);
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

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
