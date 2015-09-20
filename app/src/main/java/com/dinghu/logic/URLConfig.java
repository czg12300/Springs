package com.dinghu.logic;

/**
 * 描述：请求配置
 *
 * @author jake
 * @since 2015/9/19 16:47
 */
public class URLConfig {

    public static final String SERVER_RELEASE = "http://h5.lefeifan.com/";
    public static final String SERVER_TEST = "http://dinghuapp.yfs.pub/";
    public static String SERVER = SERVER_TEST;

    static {
        if (AppConfig.IS_DEBUG) {
            SERVER = SERVER_TEST;
        } else {
            SERVER = SERVER_RELEASE;
        }
    }

    public static final String LOGIN = SERVER + "employ/login";
    public static final String MODIFY_PW = SERVER + "employ/updatePwd";
    public static final String WORK_LIST_TODO = SERVER + "outForm/unFinish";
    public static final String WORK_LIST_TODAY = SERVER + "outForm/todayFinish";
    public static final String WORK_LIST_HISTORY = SERVER + "outForm/history";
    public static final String WORK_LIST_DETAIL = SERVER + "outForm/detail";
    public static final String DETAIL_SENDGOODS = SERVER + "outForm/sendGoods";
    public static final String DETAIL_GAINGOODS = SERVER + "outForm/gainGoods";
}
