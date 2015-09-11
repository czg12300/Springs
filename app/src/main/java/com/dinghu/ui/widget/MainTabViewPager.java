
package com.dinghu.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class MainTabViewPager extends ViewPager {

    public MainTabViewPager(Context context) {
        super(context);
    }

    public MainTabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v.getClass().getName().equals("com.dinghu.ui.widget.WorkListViewPager")) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
