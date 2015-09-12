package com.dinghu.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.logic.entity.WorkListInfo;

import cn.common.ui.adapter.BaseListAdapter;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 10:34
 */
public class WorkListAdapter extends BaseListAdapter<WorkListInfo> implements View.OnClickListener {
    public WorkListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflate(R.layout.item_work_list);
            convertView.setOnClickListener(this);
            holder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tvContentLine1 = (TextView) convertView.findViewById(R.id.tv_content_line1);
            holder.tvContentLine2 = (TextView) convertView.findViewById(R.id.tv_content_line2);
            holder.tvContentLine3 = (TextView) convertView.findViewById(R.id.tv_content_line3);
            holder.tvContentLine4 = (TextView) convertView.findViewById(R.id.tv_content_line4);
            convertView.setTag(R.layout.item_work_list, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.layout.item_work_list);
        }
        WorkListInfo info = mDataList.get(position);
        if (info != null) {
            convertView.setTag(info);
            if (position < 100) {
                holder.tvIndex.setVisibility(View.VISIBLE);
                holder.tvIndex.setText("" + (position + 1));
            } else {
                holder.tvIndex.setVisibility(View.GONE);
            }
            holder.tvTitle.setText(getTitle(info));
            if (!TextUtils.isEmpty(info.deliveryAddress)) {
                holder.tvAddress.setText(info.deliveryAddress);
            }
            holder.tvContentLine1.setText(getContentLine1(info));
            holder.tvContentLine2.setText(getContentLine2(info));
            holder.tvContentLine3.setText(getContentLine3(info));
            holder.tvContentLine4.setText(getContentLine4(info));
        }
        return convertView;
    }

    private String getContentLine1(WorkListInfo info) {
        StringBuilder builder = new StringBuilder("长途：");
        builder.append(info.isFar ? "是" : "否");
        builder.append("    高层：");
        builder.append(info.isHigh ? "是" : "否");
        builder.append("    欠桶：" + info.owePail);
        builder.append("    欠数：" + info.oweNum);
        return builder.toString();
    }

    private String getContentLine2(WorkListInfo info) {
        return "天然氧吧  五加仑  2桶";
    }

    private String getContentLine3(WorkListInfo info) {
        return "要求时间：" + info.requestTime;
    }

    private String getContentLine4(WorkListInfo info) {
        return "完工时间：" + "2015-9-12  11:33";
    }

    private String getTitle(WorkListInfo info) {
        return "【配送】 " + info.deliveryStaff + "     " + info.deliveryNum;
    }

    @Override
    public void onClick(View v) {
        //TODO 进入详情页
        WorkListInfo info = (WorkListInfo) v.getTag();
    }

    final class ViewHolder {
        TextView tvIndex;
        TextView tvTitle;
        TextView tvAddress;
        TextView tvContentLine1;
        TextView tvContentLine2;
        TextView tvContentLine3;
        TextView tvContentLine4;
    }
}
