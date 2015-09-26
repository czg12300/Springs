
package com.dinghu.ui.fragment.worklist;

import android.content.Intent;
import android.provider.SyncStateContract;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.dinghu.R;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.WorkListResponse;
import com.dinghu.ui.activity.WorkListDetailActivity;
import com.dinghu.ui.adapter.WorkListAdapter;
import com.dinghu.ui.widget.xlistview.XListView;

import java.util.List;

import cn.common.ui.adapter.BaseListAdapter;

/**
 * 描述：未完工工单页面
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public class TodoListFragment extends BaseWorkListFragment<WorkListInfo> {
    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    protected void addMapMarker(List<WorkListInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(34.341568, 108.940174), 18, 0, 30)), 500, null);
            }
            addMapMark(list.get(i));
        }
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(marker.getPosition(), 18, 0, 30)), 500, null);
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });
        mAMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    WorkListInfo info = (WorkListInfo) marker.getObject();
                    Intent it = new Intent(getActivity(), WorkListDetailActivity.class);
                    it.putExtra("WorkListId", info.getId());
                    startActivity(it);
                }
            }
        });
    }

    int i = 1;

    private void addMapMark(WorkListInfo info) {
        // 设置Marker的图标样式
        MarkerOptions markerOptions = new MarkerOptions();
        switch (info.getTimeType()) {
            case WorkListInfo.TIME_TYPE_IN:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_point_blue));
                break;
            case WorkListInfo.TIME_TYPE_OUT_LESS_FIVE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_point_violet));
                break;
            case WorkListInfo.TIME_TYPE_OUT_MORE_FIVE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_point_red));
                break;
        }
        // 设置Marker点击之后显示的标题
        markerOptions.title(info.getAddress());
        // 设置Marker的坐标，为我们点击地图的经纬度坐标
        markerOptions.position(new LatLng(34.341568 + i, 108.940174 + i));
        // 设置Marker的可见性
        markerOptions.visible(true);
        // 设置Marker是否可以被拖拽，这里先设置为false，之后会演示Marker的拖拽功能
        markerOptions.perspective(true);
        markerOptions.draggable(false);
        // 将Marker添加到地图上去
        mAMap.addMarker(markerOptions).setObject(info);
        i++;
    }

    @Override
    protected List<WorkListInfo> loadData() {
        HttpRequestManager<WorkListResponse> request = new HttpRequestManager<WorkListResponse>(URLConfig.WORK_LIST_TODO, WorkListResponse.class);
        request.addParam("pageNum", getPageIndex() + "");
        request.addParam("pageSize", getPageSize() + "");
        request.addParam("employId", InitShareData.getUserId() + "");
        WorkListResponse response = request.sendRequest();
        if (response != null) {
            return response.getList();
        }
        return null;
    }

    @Override
    protected void addFooter(XListView mLvList) {

    }

    @Override
    protected void addHeader(XListView mLvList) {

    }

    @Override
    protected BaseListAdapter<WorkListInfo> createAdapter() {
        return new WorkListAdapter(getActivity()).setIsTodo(true);
    }

}
