
package com.dinghu.logic.http.response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.common.http.base.BaseResponse;

/**
 * 描述:消息推送，返回数据
 *
 * @author jakechen
 * @since 2015/9/21 11:51
 */
public class MessageResponse extends BaseResponse {
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public Object parse(String json) {
        try {
            JSONObject root = new JSONObject(json);
            setCount(root.optInt("count"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        setIsOk(true);
        return this;
    }
}
