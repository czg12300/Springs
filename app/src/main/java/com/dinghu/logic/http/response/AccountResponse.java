
package com.dinghu.logic.http.response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.common.http.base.BaseResponse;

/**
 * 描述：我的账单返回数据
 *
 * @author jake
 * @since 2015/9/27 22:08
 */
public class AccountResponse extends BaseResponse {
    private int ps_ssCount;

    private int ps_formTotal;

    private int ps_ct;

    private int ps_gc;

    private int ps_emptyCount;

    private int tc_formTotal;

    private double tc_jkAmount;

    private double tc_skAmount;

    private String storeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getPs_ssCount() {
        return ps_ssCount;
    }

    public void setPs_ssCount(int ps_ssCount) {
        this.ps_ssCount = ps_ssCount;
    }

    public int getPs_formTotal() {
        return ps_formTotal;
    }

    public void setPs_formTotal(int ps_formTotal) {
        this.ps_formTotal = ps_formTotal;
    }

    public int getPs_ct() {
        return ps_ct;
    }

    public void setPs_ct(int ps_ct) {
        this.ps_ct = ps_ct;
    }

    public int getPs_gc() {
        return ps_gc;
    }

    public void setPs_gc(int ps_gc) {
        this.ps_gc = ps_gc;
    }

    public double getTc_jkAmount() {
        return tc_jkAmount;
    }

    public void setTc_jkAmount(double tc_jkAmount) {
        this.tc_jkAmount = tc_jkAmount;
    }

    public int getPs_emptyCount() {
        return ps_emptyCount;
    }

    public void setPs_emptyCount(int ps_emptyCount) {
        this.ps_emptyCount = ps_emptyCount;
    }

    public double getTc_formTotal() {
        return tc_formTotal;
    }

    public void setTc_formTotal(int tc_formTotal) {
        this.tc_formTotal = tc_formTotal;
    }

    public double getTc_skAmount() {
        return tc_skAmount;
    }

    public void setTc_skAmount(double tc_skAmount) {
        this.tc_skAmount = tc_skAmount;
    }

    @Override
    public Object parse(String json) {
        try {
            JSONObject root = new JSONObject(json);
            setPs_ssCount(root.optInt("ps_ssCount"));
            setPs_formTotal(root.optInt("ps_formTotal"));
            setPs_ct(root.optInt("ps_ct"));
            setPs_gc(root.optInt("ps_gc"));
            setPs_emptyCount(root.optInt("ps_emptyCount"));
            setTc_formTotal(root.optInt("tc_formTotal"));
            setTc_jkAmount(root.optDouble("tc_jkAmount"));
            setTc_skAmount(root.optDouble("tc_skAmount"));
            setStoreName(root.optString("mendian"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        setIsOk(true);
        return this;
    }
}
