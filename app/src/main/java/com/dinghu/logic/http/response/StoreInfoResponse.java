package com.dinghu.logic.http.response;

import com.dinghu.logic.entity.StoreInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.common.http.base.BaseResponse;

import java.util.ArrayList;

/**
 * 描述:
 *
 * @author jakechen
 * @since 2015/9/30 14:34
 */
public class StoreInfoResponse extends BaseResponse {
  public static final int CODE_FAIL = 0;

  public static final int CODE_SUCCESS = 1;
  private int code;

  private String msg;

  private ArrayList<StoreInfo> storesInfoList;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public ArrayList<StoreInfo> getStoresInfoList() {
    return storesInfoList;
  }

  public void setStoresInfoList(ArrayList<StoreInfo> storesInfoList) {
    this.storesInfoList = storesInfoList;
  }

  @Override
  public Object parse(String json) {
    // sta:[{name:中关村,id:120},{name:中关村,id:120},{name:中关村,id:120}]
    try {
      JSONObject root = new JSONObject(json);
      setCode(root.optInt("code"));
      setMsg(root.optString("msg"));
      JSONArray array = root.optJSONArray("sta");
      if (array != null && array.length() > 0) {
        ArrayList<StoreInfo> list = new ArrayList<StoreInfo>();
        StoreInfo info;
        for (int i = 0; i < array.length(); i++) {
          JSONObject obj = array.optJSONObject(i);
          if (obj != null) {
            info = new StoreInfo();
            info.setName(obj.optString("name"));
            info.setId(obj.optLong("id"));
            list.add(info);
          }
        }
        setStoresInfoList(list);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    setIsOk(true);
    return this;
  }
}
