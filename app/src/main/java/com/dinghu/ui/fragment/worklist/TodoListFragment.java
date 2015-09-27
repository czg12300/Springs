
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
        mMapViewHelper.addMapMarker(list);
        mMapViewHelper.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });
        mMapViewHelper.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
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
