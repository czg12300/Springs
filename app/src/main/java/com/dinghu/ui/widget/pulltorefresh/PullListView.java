
package com.dinghu.ui.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.dinghu.R;

import cn.common.ui.widgt.pulltorefresh.BasePullListView;

/**
 * 描述:
 *
 * @author jakechen
 * @since 2015/9/17 17:48
 */
public class PullListView extends BasePullListView {
    public PullListView(Context context) {
        this(context, null);

    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHeaderView(inflate(context, R.layout.layout_pull_listview_header, null));
        setFooterView(inflate(context, R.layout.layout_pull_listview_header, null));
    }

    @Override
    protected void handleRefreshing(View header) {
    }

    @Override
    protected void handleLoading(View footer) {
    }
}
