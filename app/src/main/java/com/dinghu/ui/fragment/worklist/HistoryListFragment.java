
package com.dinghu.ui.fragment.worklist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.activity.WorkListDetailActivity;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.helper.MapViewHelper;
import com.dinghu.ui.widget.xlistview.XListView;
import com.dinghu.utils.ToastUtil;

import java.text.SimpleDateFormat;
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
    private MapViewHelper mMapViewHelper;

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
    private MapView mMapView;
    private View mVMap;
    private LinearLayout mLlList;

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
            return response.getList();
        }
        return null;
    }

    @Override
    public void setContentView(View view) {
        View root = inflate(R.layout.fragment_history);
        mLlList = (LinearLayout) root.findViewById(R.id.ll_list);
        mLlList.addView(view, new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        super.setContentView(root);
        initListView();
        initMapView();
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
        mEndDate = getCurrentTime();
        updateStartTime();
        updateEndTime();
    }

    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.mv_map);
        mVMap = findViewById(R.id.fl_map);
        findViewById(R.id.iv_locate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapViewHelper.startLocate();
            }
        });
        findViewById(R.id.iv_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapViewHelper.zoomIn();
            }
        });
        findViewById(R.id.iv_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapViewHelper.zoomOut();
            }
        });
        mMapView.onCreate(mSavedInstanceState);// 此方法必须重写
        mMapViewHelper = new MapViewHelper(mMapView);
    }

    @Override
    protected void addFooter(XListView mLvList) {
    }

    @Override
    protected void addHeader(XListView mLvList) {
    }

    @Override
    public void setupBroadcastActions(List<String> actions) {
        super.setupBroadcastActions(actions);
        actions.add(BroadcastActions.ACTION_CHANGE_MODE);
    }

    @Override
    public void handleBroadcast(Context context, Intent intent) {
        super.handleBroadcast(context, intent);
        if (TextUtils.equals(intent.getAction(), BroadcastActions.ACTION_CHANGE_MODE)) {
            if (intent.getBooleanExtra("IsMapMode", false)) {
                mVMap.setVisibility(View.VISIBLE);
                mLlList.setVisibility(View.GONE);
            } else {
                mVMap.setVisibility(View.GONE);
                mLlList.setVisibility(View.VISIBLE);
            }
        }
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
        if (msg.what == MSG_UI_LOAD_SUCCESS) {
            mMapViewHelper.addMapMarker((List<WorkListInfo>) msg.obj);
        }
        super.handleUiMessage(msg);
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

}
