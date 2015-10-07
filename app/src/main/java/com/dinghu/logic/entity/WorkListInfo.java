
package com.dinghu.logic.entity;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 10:35
 */
public class WorkListInfo {
    public static final int TIME_TYPE_IN = 1;

    public static final int TIME_TYPE_OUT_LESS_FIVE = 2;

    public static final int TIME_TYPE_OUT_MORE_FIVE = 3;

    public static final String TYPE_PEISONG = "配送";

    public static final String TYPE_TAOCAN = "套餐";

    private long id;

    private int moneyOrCount;
    private int timeType;

    private String type;

    private String address;

    private String tel;

    private String name;

    private String goods;

    private String finishTime;

    private String requireTime;

    private double lat;

    private double lng;

    private int moneyOrCount2;

    public int getMoneyOrCount2() {
        return moneyOrCount2;
    }

    public void setMoneyOrCount2(int moneyOrCount2) {
        if (moneyOrCount2 < 0) {
            moneyOrCount2 = 0;
        }
        this.moneyOrCount2 = moneyOrCount2;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
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

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getRequireTime() {
        return requireTime;
    }

    public void setRequireTime(String requireTime) {
        this.requireTime = requireTime;
    }
}
