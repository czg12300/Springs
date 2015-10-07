
package com.dinghu.logic.http.response;

import com.dinghu.logic.entity.WorkListInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.common.http.base.BaseResponse;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 10:35
 */
public class WorkListResponse extends BaseResponse {
    private List<WorkListInfo> list;

    public List<WorkListInfo> getList() {
        return list;
    }

    public void setList(List<WorkListInfo> list) {
        this.list = list;
    }

    @Override
    public WorkListResponse parse(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray != null && jsonArray.length() > 0) {
                List<WorkListInfo> list = new ArrayList<WorkListInfo>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject root = jsonArray.optJSONObject(i);
                    if (root != null) {
                        WorkListInfo info = new WorkListInfo();
                        info.setId(root.optLong("id"));
                        info.setTimeType(root.optInt("timeType"));
                        info.setMoneyOrCount(root.optInt("moneyOrCount"));
                        info.setMoneyOrCount2(root.optInt("moneyOrCount2"));
                        info.setAddress(root.optString("address"));
                        info.setTel(root.optString("tel"));
                        info.setName(root.optString("name"));
                        info.setGoods(root.optString("goods"));
                        info.setFinishTime(root.optString("finishTime"));
                        info.setType(root.optString("type"));
                        info.setRequireTime(root.optString("requireTime"));
                        info.setLat(root.optDouble("lat"));
                        info.setLng(root.optDouble("lng"));
                        list.add(info);
                    }
                }
                setList(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        setIsOk(true);
        return this;
    }
}
