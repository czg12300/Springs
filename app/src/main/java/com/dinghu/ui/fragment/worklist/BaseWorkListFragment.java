
package com.dinghu.ui.fragment.worklist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

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
import com.amap.api.maps.model.MyLocationStyle;
import com.dinghu.R;
import com.dinghu.data.BroadcastActions;
import com.dinghu.ui.widget.StatusView;
import com.dinghu.ui.widget.xlistview.XListView;
import com.dinghu.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.common.ui.adapter.BaseListAdapter;
import cn.common.ui.fragment.BaseWorkerFragment;
import cn.common.utils.BitmapUtil;

/**
 * 描述：未完工工单页面
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public abstract class BaseWorkListFragment<T> extends BaseWorkerFragment
        implements XListView.IXListViewListener, AMapLocationListener {

    private static final int START_PAGE_INDEX = 1;

    private static final int MSG_BACK_LOAD = 1000;

    private static final int MSG_UI_LOAD_FAIL = 1001;

    private static final int MSG_UI_LOAD_SUCCESS = 1002;

    private static final int MSG_UI_FINISH_LOAD_ALL = 1003;

    protected XListView mLvList;

    private int mPageIndex = START_PAGE_INDEX;

    private int mPageSize = 10;

    private BaseListAdapter<T> mAdapter;

    protected void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    protected int getPageIndex() {
        return mPageIndex;
    }

    protected int getPageSize() {
        return mPageSize;
    }

    private StatusView mStatusView;

    private boolean isInit = false;

    protected AMap mAMap;
    protected MapView mMapView;
    private View mVMap;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;

    private LocationManagerProxy mAMapLocationManager;

    @Override
    protected void initView() {
        mStatusView = new StatusView(getActivity());
        mStatusView.setContentView(R.layout.fragment_base_work_list);
        setContentView(mStatusView);
        initMapView();
        initXListView();
        mStatusView.setStatusListener(new StatusView.StatusListener() {
            @Override
            public void onLoad() {
                sendEmptyBackgroundMessageDelayed(MSG_BACK_LOAD, 2000);
            }
        });
        mStatusView.showLoadingView();
        sendEmptyBackgroundMessageDelayed(MSG_BACK_LOAD, 2000);
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
                mAMap.animateCamera(CameraUpdateFactory.zoomIn(), 500, null);
            }
        });
        findViewById(R.id.iv_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAMap.animateCamera(CameraUpdateFactory.zoomOut(), 500, null);
            }
        });
        mMapView.onCreate(mSavedInstanceState);// 此方法必须重写
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        uiSettings.setZoomControlsEnabled(false);
        initLocate();
    }

    private void initXListView() {
        mLvList = (XListView) findViewById(R.id.lv_list);
        mLvList.setPullLoadEnable(true);
        addHeader(mLvList);
        mLvList.setXListViewListener(this);
        mAdapter = createAdapter();
        mLvList.setAdapter(mAdapter);
        addFooter(mLvList);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInit) {
            if (mStatusView != null) {

                isInit = true;
            }
        }
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case MSG_UI_LOAD_FAIL:
                if (mAdapter.getCount() == 0 && mPageIndex == START_PAGE_INDEX) {
                    mStatusView.showFailView();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                } else if (mPageIndex == START_PAGE_INDEX) {
                    mStatusView.showContentView();
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                } else {
                    mStatusView.showContentView();
                    mLvList.stopLoadMore(false);
                }
                break;
            case MSG_UI_LOAD_SUCCESS:
                mStatusView.showContentView();
                List<T> list = (List<T>) msg.obj;
                addMapMarker(list);
                if (mPageIndex == START_PAGE_INDEX) {
                    mAdapter.setData(list);
                    mLvList.setRefreshTime(getCurrentTime());
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(false);
                } else {
                    mLvList.setRefreshTime(getCurrentTime());
                    mLvList.stopRefresh();
                    mAdapter.addAll(list);
                    mLvList.stopLoadMore(false);
                }
                break;
            case MSG_UI_FINISH_LOAD_ALL:
                mStatusView.showContentView();
                if (mPageIndex == START_PAGE_INDEX) {
                    mAdapter.setData((List<T>) msg.obj);
                    mLvList.setRefreshTime(getCurrentTime());
                    mLvList.stopRefresh();
                    mLvList.stopLoadMore(true);
                } else {
                    mAdapter.addAll((List<T>) msg.obj);
                    mLvList.stopLoadMore(true);
                }
                break;
        }
    }

    /**
     * 添加地图显示
     *
     * @param list
     */
    protected abstract void addMapMarker(List<T> list);

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    @Override
    public void handleBackgroundMessage(Message msg) {
        super.handleBackgroundMessage(msg);
        List<T> list = loadData();
        if (list != null && list.size() >= mPageSize) {
            Message message = obtainUiMessage();
            message.what = MSG_UI_LOAD_SUCCESS;
            message.obj = list;
            message.sendToTarget();
        } else if (mPageIndex > START_PAGE_INDEX && list == null
                || list != null && list.size() < mPageSize) {
            Message message = obtainUiMessage();
            message.what = MSG_UI_FINISH_LOAD_ALL;
            message.obj = list;
            message.sendToTarget();
        } else if (mPageIndex == START_PAGE_INDEX) {
            sendEmptyUiMessage(MSG_UI_LOAD_FAIL);
        }

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
                mLvList.setVisibility(View.GONE);
            } else {
                mVMap.setVisibility(View.GONE);
                mLvList.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh() {
        mPageIndex = START_PAGE_INDEX;
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);

    }

    @Override
    public void onLoadMore() {
        mPageIndex++;
        sendEmptyBackgroundMessage(MSG_BACK_LOAD);
    }

    protected abstract List<T> loadData();

    protected abstract void addFooter(XListView mLvList);

    protected abstract void addHeader(XListView mLvList);

    protected abstract BaseListAdapter<T> createAdapter();

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
//            mAMap.setMyLocationRotateAngle(mAMap.getCameraPosition().bearing);// 设置小蓝点旋转角度
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(aLocation.getLatitude(), aLocation.getLongitude()), 18, 0, 30)), 500, null);
        }
//        if (mOnLocationChangedListener != null && aLocation != null) {
//            ToastUtil.show(aLocation.getCity() + aLocation.getAddress() + aLocation.getPoiName());
//            mOnLocationChangedListener.onLocationChanged(aLocation);// 显示系统小蓝点
//            mAMap.setMyLocationRotateAngle(mAMap.getCameraPosition().bearing);// 设置小蓝点旋转角度
//            LatLng latLng = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
//            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.5f));
//
//        }
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
