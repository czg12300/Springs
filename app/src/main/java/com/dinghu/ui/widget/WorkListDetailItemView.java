package com.dinghu.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dinghu.R;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/20 10:52
 */
public class WorkListDetailItemView extends LinearLayout {
    private TextView mTvLabel;
    private TextView mTvContent;
    private View vDivider;

    public WorkListDetailItemView(Context context) {
        this(context, null);
    }

    public WorkListDetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.view_work_list_detail_item, this);
        mTvLabel = (TextView) findViewById(R.id.tv_label);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        vDivider = findViewById(R.id.v_divider);
    }

    public TextView getTvLabel() {
        return mTvLabel;
    }

    public TextView getTvContent() {
        return mTvContent;
    }

    public void showDivider(boolean isShow) {
        vDivider.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setLabel(int resId) {
        setLabel(getResources().getString(resId));
    }

    public void setLabel(CharSequence cs) {
        if (!TextUtils.isEmpty(cs) && mTvLabel != null) {
            mTvLabel.setText(cs);
        }
    }

    public void setContent(String cs) {
        if (!TextUtils.isEmpty(cs) && mTvContent != null) {
            mTvContent.setText(cs);
        }
    }


}
