
package com.dinghu.ui.fragment.worklist;

import com.amap.api.maps.MapView;
import com.dinghu.R;
import com.dinghu.ui.helper.MapViewHelper;

import android.os.Bundle;
import android.view.View;

/**
 * 描述：未完工工单页面
 *
 * @author jake
 * @since 2015/9/12 13:57
 */
public abstract class BaseWorkListFragment<T> extends BaseListFragment<T> {

    protected MapView mMapView;

    protected View mVMap;

    protected MapViewHelper mMapViewHelper;

    @Override
    protected void initView() {
        super.initView();
        initMapView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_base_work_list;
    }

    /**
     * 初始化地图
     */
    protected void initMapView() {
        mMapView = (MapView) findViewById(R.id.mv_map);
        mMapView.onCreate(mSavedInstanceState);// 此方法必须重写
        mVMap = findViewById(R.id.fl_map);
        mMapViewHelper = new MapViewHelper(mMapView);
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
