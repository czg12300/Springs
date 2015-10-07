
package com.dinghu.ui.fragment.worklist;

import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.widget.xlistview.XListView;
import com.dinghu.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.common.ui.BaseDialog;
import cn.common.ui.adapter.BaseListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 描述：历史工单页面
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public class HistoryListFragment extends BaseListFragment<WorkListInfo> {

    private BaseDialog mLoadingDialog;

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
        HttpRequestManager<WorkListResponse> request = new HttpRequestManager<WorkListResponse>(
                URLConfig.WORK_LIST_HISTORY, WorkListResponse.class);
        request.addParam("pageNum", getPageIndex() + "");
        request.addParam("pageSize", getPageSize() + "");
        request.addParam("employId", InitShareData.getUserId() + "");
        request.addParam("startDate", mStartDate);
        request.addParam("endDate", mEndDate);
        WorkListResponse response = request.sendRequest();
        if (response != null) {
            if (response.isOk() && response.getList() == null) {
                return new ArrayList<WorkListInfo>();
            }
            return response.getList();
        }
        return null;
    }

    @Override
    public void setContentView(View view) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inflate(R.layout.header_list_history));
        layout.addView(view, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        super.setContentView(layout);
        initListView();
    }

    private void initListView() {
        mTvStart = (TextView) findViewById(R.id.tv_date_start);
        mTvEnd = (TextView) findViewById(R.id.tv_date_end);
        findViewById(R.id.fl_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateSelectDialog(START);
            }
        });
        findViewById(R.id.fl_end).setOnClickListener(new View.OnClickListener() {
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
        mEndDate = Utils.getCurrentTime();
        updateStartTime();
        updateEndTime();
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

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), BroadcastActions.ACTION_CHANGE_MODE)) {
        }
    }

    private void showDateSelectDialog(String tag) {
        if (canShowDialog()) {
            if (mDateSelectDialog == null) {
                mDateSelectDialog = new BaseDialog(getActivity());
                mDateSelectDialog.setWindow(R.style.alpha_animation, 0.3f);
                mDateSelectDialog.setContentView(R.layout.dialog_select_date);
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
                                mStartDate = mDatePicker.getYear() + "-"
                                        + (mDatePicker.getMonth() + 1) + "-"
                                        + mDatePicker.getDayOfMonth();
                                updateStartTime();
                            } else if (TextUtils.equals(type, END)) {
                                mEndDate = mDatePicker.getYear() + "-"
                                        + (mDatePicker.getMonth() + 1) + "-"
                                        + mDatePicker.getDayOfMonth();
                                updateEndTime();
                            }
                            onRefresh();
                        }
                        if (mDateSelectDialog != null) {
                            mDateSelectDialog.dismiss();
                        }
                        showLoadingDialog();
                    }
                });
            }
            if (TextUtils.equals(tag, START)) {
                int[] ints = getTime(mStartDate);
                mTvTitle.setText("请选择起初日期");
                mDatePicker.updateDate(ints[0], ints[1], ints[2]);
            } else if (TextUtils.equals(tag, END)) {
                mTvTitle.setText("请选择结束日期");
                int[] ints = getTime(mEndDate);
                mDatePicker.updateDate(ints[0], ints[1], ints[2]);
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

    @Override
    public void handleUiMessage(Message msg) {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        super.handleUiMessage(msg);

    }

    public void showLoadingDialog() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new BaseDialog(getActivity());
                mLoadingDialog.setWindow(R.style.alpha_animation, 0.0f);
                mLoadingDialog.setContentView(R.layout.dialog_loading);
            }
            mLoadingDialog.show();
        }
    }
}
