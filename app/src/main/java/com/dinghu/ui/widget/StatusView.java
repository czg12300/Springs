package com.dinghu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.dinghu.R;

/**
 * 描述：状态显示页面
 *
 * @author jake
 * @since 2015/9/19 15:00
 */
public class StatusView extends FrameLayout {
    public StatusView(Context context) {
        this(context, null);
    }

    private View mContentView;
    private View mLoadView;
    private View mFailView;
    private View mNoDataView;

    public static interface StatusListener {
        void onLoad();
    }

    private StatusListener mListener;

    public void setStatusListener(StatusListener listener) {
        mListener = listener;
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_status_tip, this);
        mLoadView = findViewById(R.id.ll_loading);
        mNoDataView = findViewById(R.id.ll_no_data);
        mNoDataView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView();
                mListener.onLoad();
            }
        });
        mFailView = findViewById(R.id.ll_fail);
        mFailView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView();
                mListener.onLoad();
            }
        });
    }

    public void showLoadingView() {
        mContentView.setVisibility(GONE);
        mFailView.setVisibility(GONE);
        mNoDataView.setVisibility(GONE);
        mLoadView.setVisibility(VISIBLE);
    }

    public void showContentView() {
        mContentView.setVisibility(VISIBLE);
        mFailView.setVisibility(GONE);
        mLoadView.setVisibility(GONE);
        mNoDataView.setVisibility(GONE);
    }

    public void showFailView() {
        mContentView.setVisibility(GONE);
        mFailView.setVisibility(VISIBLE);
        mLoadView.setVisibility(GONE);
        mNoDataView.setVisibility(GONE);
    }

    public void showNoDataView() {
        mContentView.setVisibility(GONE);
        mFailView.setVisibility(GONE);
        mLoadView.setVisibility(GONE);
        mNoDataView.setVisibility(VISIBLE);
    }

    public void setContentView(View vContent) {
        mContentView = vContent;
        addView(vContent);
    }

    public void setContentView(int layoutId) {
        setContentView(inflate(getContext(), layoutId, null));
    }
}
