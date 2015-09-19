package com.dinghu.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinghu.R;
import com.dinghu.logic.entity.WorkListInfo;
import com.dinghu.logic.http.response.WorkListResponse;

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

    private boolean isTodo = false;

    public boolean isTodo() {
        return isTodo;
    }

    public WorkListAdapter setIsTodo(boolean isTodo) {
        this.isTodo = isTodo;
        return this;
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
            if (isTodo) {
                switch (info.getTimeType()) {
                    case WorkListInfo.TIME_TYPE_IN:
                        holder.tvIndex.setBackgroundResource(R.drawable.bg_black_r);
                        holder.tvTitle.setTextColor(getColor(R.color.black_424242));
                        break;
                    case WorkListInfo.TIME_TYPE_OUT_LESS_FIVE:
                        holder.tvIndex.setBackgroundResource(R.drawable.bg_purple_r);
                        holder.tvTitle.setTextColor(getColor(R.color.purple_be2bbc));
                        break;
                    case WorkListInfo.TIME_TYPE_OUT_MORE_FIVE:
                        holder.tvIndex.setBackgroundResource(R.drawable.bg_red_r);
                        holder.tvTitle.setTextColor(getColor(R.color.red_fa5e51));
                        break;
                }
            }
            if (position < 100) {
                holder.tvIndex.setVisibility(View.VISIBLE);
                holder.tvIndex.setText("" + (position + 1));
            } else {
                holder.tvIndex.setVisibility(View.GONE);
            }
            holder.tvTitle.setText(getTitle(info));
            if (!TextUtils.isEmpty(info.getAddress())) {
                holder.tvAddress.setText(info.getAddress());
            }
//            holder.tvContentLine1.setText(getContentLine1(info));
            holder.tvContentLine2.setText(getContentLine2(info));
            holder.tvContentLine3.setText(getContentLine3(info));
            holder.tvContentLine4.setText(getContentLine4(info));
        }
        return convertView;
    }


    private String getContentLine2(WorkListInfo info) {
        String reslut = "";
        if (!TextUtils.isEmpty(info.getGoods())) {
            reslut = reslut + info.getGoods();
        }
        if (TextUtils.equals(info.getType(), WorkListInfo.TYPE_TAOCAN)) {
            reslut = reslut + "   " + info.getMoneyOrCount() + "元";
        } else if (TextUtils.equals(info.getType(), WorkListInfo.TYPE_PEISONG)) {
            reslut = reslut + "   " + info.getMoneyOrCount() + "桶";
        }
        return reslut;
    }

    private String getContentLine3(WorkListInfo info) {
        return "要求时间：" + info.getRequireTime();
    }

    private String getContentLine4(WorkListInfo info) {
        return "完工时间：" + info.getFinishTime();
    }

    private String getTitle(WorkListInfo info) {
        return info.getType() + "   " + info.getName() + "     " + info.getId();
    }

    @Override
    public void onClick(View v) {
        //TODO 进入详情页
        WorkListResponse info = (WorkListResponse) v.getTag();
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
