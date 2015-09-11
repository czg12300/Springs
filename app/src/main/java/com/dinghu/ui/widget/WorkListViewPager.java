
package com.dinghu.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class WorkListViewPager extends ViewPager {

    public WorkListViewPager(Context context) {
        super(context);
    }

    public WorkListViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v.getClass().getName().equals("com.amap.api.maps.MapView")) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
