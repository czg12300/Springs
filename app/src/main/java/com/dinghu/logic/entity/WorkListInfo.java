package com.dinghu.logic.entity;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/12 10:35
 */
public class WorkListInfo implements JsonParse {
    public long id = 0;
    public String deliveryNum = "";
    public String deliveryStaff = "";
    public String deliveryAddress = "";
    public String other = "";
    public String requestTime = "";
    public String finishTime = "";
    public boolean isFar = false;
    public boolean isHigh = false;
    public int owePail = 0;
    public int oweNum = 0;

    @Override
    public void parse(String json) {

    }
}
