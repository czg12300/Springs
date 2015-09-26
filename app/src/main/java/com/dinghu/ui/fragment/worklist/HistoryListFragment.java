
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
import com.amap.api.maps.model.LatLng;
import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.adapter.WorkListAdapter;
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
public class HistoryListFragment extends BaseListFragment<WorkListInfo> implements AMapLocationListener {
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
    private AMap mAMap;
    private MapView mMapView;
    private View mVMap;
    private LinearLayout mLlList;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;

    private LocationManagerProxy mAMapLocationManager;

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
                startLocate();
            }
        });
        findViewById(R.id.iv_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        findViewById(R.id.iv_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });
        mMapView.onCreate(mSavedInstanceState);// 此方法必须重写
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        uiSettings.setZoomControlsEnabled(false);
//        initLocate();
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

    protected void addMapMarker(List<WorkListInfo> list) {

    }

    @Override
    public void handleUiMessage(Message msg) {
        if (msg.what == MSG_UI_LOAD_SUCCESS) {
            addMapMarker((List<WorkListInfo>) msg.obj);
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


    /**
     * 设置一些amap的属性
     */
    private void initLocate() {
//        setMyLocationStyle();
        mAMap.setMyLocationRotateAngle(180);
        mAMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mOnLocationChangedListener = onLocationChangedListener;
                startLocate();
            }

            @Override
            public void deactivate() {
                stopLocate();
                mOnLocationChangedListener = null;
            }
        });
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

//    /**
//     * 自定义定位的样式
//     */
//    private void setMyLocationStyle() {
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.decodeResource(
//                R.drawable.ico_point_blue, (int)
//                        getDimension(R.dimen.title_height),
//                (int) getDimension(R.dimen.title_height))));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(getColor(R.color.red_f03636));//
//        myLocationStyle.radiusFillColor(getColor(R.color.red_f03636));//
//        myLocationStyle.strokeWidth(2f);// 设置圆形的边框粗细
//        mAMap.setMyLocationStyle(myLocationStyle);
//    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mOnLocationChangedListener != null && aLocation != null) {
            ToastUtil.show(aLocation.getCity() + aLocation.getAddress() + aLocation.getPoiName());
            mOnLocationChangedListener.onLocationChanged(aLocation);// 显示系统小蓝点
            mAMap.setMyLocationRotateAngle(mAMap.getCameraPosition().bearing);// 设置小蓝点旋转角度
            LatLng latLng = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.5f));

        }
    }

    /**
     * 开始定位
     */
    protected void startLocate() {
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
        }
        /*
         * mAMapLocManager.setGpsEnable(false);
         * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
         * API定位采用GPS和网络混合定位方式
         * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
         */
        mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 10, this);
    }

    /**
     * 停止定位
     */
    protected void stopLocate() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
            mAMapLocationManager = null;
        }
    }
}
