
package com.dinghu.logic.http.response;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import cn.common.http.base.BaseResponse;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 10:35
 */
public class WorkListDetailResponse extends BaseResponse {
    public static final int STATUS_QUHUO = 1;

    public static final int STATUS_WANGONG = 2;

    public static final int STATUS_WAIT = 3;

    public static final int BUTTON_SHOW_BUTTON = 1;

    public static final int BUTTON_NOT_SHOW = 0;

    private long id;

    private String address;

    private String tel;

    private String name;

    private int moneyOrCount;

    private String goods;

    private String time;

    private String type;

    private String btnMsg;

    private String report;

    private int status;

    private int moneyOrCount2;

    private int hasBtn;

    private String btnName;

    private String confirmInfo;

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getConfirmInfo() {
        return confirmInfo;
    }

    public void setConfirmInfo(String confirmInfo) {
        this.confirmInfo = confirmInfo;
    }

    public void setHasBtn(int hasBtn) {
        this.hasBtn = hasBtn;
    }

    public int getHasBtn() {
        return hasBtn;
    }

    /**
     * 判断是否显示按钮
     * 
     * @return
     */
    public boolean hasBtn() {
        return hasBtn == BUTTON_SHOW_BUTTON && !TextUtils.isEmpty(btnName)
                && !TextUtils.isEmpty(confirmInfo);
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getMoneyOrCount2() {
        return moneyOrCount2;
    }

    public void setMoneyOrCount2(int moneyOrCount2) {
        if (moneyOrCount2 < 0) {
            moneyOrCount2 = 0;
        }
        this.moneyOrCount2 = moneyOrCount2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoneyOrCount() {
        return moneyOrCount;
    }

    public void setMoneyOrCount(int moneyOrCount) {
        if (moneyOrCount < 0) {
            moneyOrCount = 0;
        }
        this.moneyOrCount = moneyOrCount;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBtnMsg() {
        return btnMsg;
    }

    public void setBtnMsg(String btnMsg) {
        this.btnMsg = btnMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public WorkListDetailResponse parse(String json) {
        try {
            JSONObject root = new JSONObject(json);
            if (root != null) {
                setId(root.optLong("id"));
                setMoneyOrCount(root.optInt("moneyOrCount"));
                setMoneyOrCount2(root.optInt("moneyOrCount2"));
                setStatus(root.optInt("status"));
                setName(root.optString("name"));
                setAddress(root.optString("address"));
                setTel(root.optString("tel"));
                setGoods(root.optString("goods"));
                setTime(root.optString("time"));
                setType(root.optString("type"));
                setBtnMsg(root.optString("btnMsg"));
                setReport(root.optString("report"));
                setHasBtn(root.optInt("hasBtn"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        setIsOk(true);
        return this;
    }
}
