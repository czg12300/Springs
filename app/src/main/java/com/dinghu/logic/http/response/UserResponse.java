package com.dinghu.logic.http.response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/19 17:06
 */
public class UserResponse extends BaseResponse {
    public static final int CODE_FAIL = 0;
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_SUCCESS_MPW = 2;


    private long userId;
    private String msg;
    private int code;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

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
    public UserResponse parse(String json) {
        try {
            JSONObject root = new JSONObject(json);
            setCode(root.optInt("code"));
            setMsg(root.optString("msg"));
            setUserId(root.optLong("id"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        setIsOk(true);
        return this;
    }
}
