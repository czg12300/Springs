
package com.dinghu.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.logic.entity.StoreInfo;

import cn.common.ui.adapter.BaseListAdapter;
import cn.common.utils.DisplayUtil;

public class StoreListAdapter extends BaseListAdapter<StoreInfo> {

    public StoreListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvContent = null;
        if (convertView == null) {
            tvContent = new TextView(getContext());
            tvContent.setTextColor(getColor(R.color.black_333333));
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(R.dimen.text_content));
            tvContent.setMinHeight((int) getDimension(R.dimen.title_height));
            tvContent.setGravity(Gravity.CENTER_VERTICAL);
            tvContent.setPadding(DisplayUtil.dip(15), 0, 0, 0);
            convertView = tvContent;
        } else {
            tvContent = (TextView) convertView;
        }
        StoreInfo info = mDataList.get(position);
        if (info != null) {
            tvContent.setText(info.getName());
        }
        return convertView;
    }
}
