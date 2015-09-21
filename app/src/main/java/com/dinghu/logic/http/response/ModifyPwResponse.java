
package com.dinghu.logic.http.response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.common.http.base.BaseResponse;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/19 17:06
 */
public class ModifyPwResponse extends BaseResponse {
    public static final int CODE_FAIL = 0;

    public static final int CODE_SUCCESS = 1;

    private String msg;

    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public ModifyPwResponse parse(String json) {
        try {
            JSONObject root = new JSONObject(json);
            setCode(root.optInt("code"));
            setMsg(root.optString("msg"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        setIsOk(true);
        return this;
    }
}
