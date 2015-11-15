
package com.dinghu.logic;

/**
 * 描述：请求配置
 *
 * @author jake
 * @since 2015/9/19 16:47
 */
public interface URLConfig {

    String SERVER_RELEASE = "http://dinghuapp.yfs.pub/";

    String SERVER_TEST = "http://192.168.0.19:8080/";

    String SERVER = AppConfig.IS_DEBUG ? SERVER_TEST : SERVER_RELEASE;

    String LOGIN = SERVER + "employ/login";

    String GET_STORE_LIST = SERVER + "employ/getStaIds";

    String MODIFY_PW = SERVER + "employ/updatePwd";

    String WORK_LIST_TODO = SERVER + "outForm/unFinish";

    String WORK_LIST_TODAY = SERVER + "outForm/todayFinish";

    String WORK_LIST_HISTORY = SERVER + "outForm/history";

    String WORK_LIST_DETAIL = SERVER + "outForm/detail";

    String DETAIL_SENDGOODS = SERVER + "outForm/sendGoods";

    String UN_FINISH_WORK = SERVER + "outForm/cannotFinish";

    String PUSH_MESSAGE = SERVER + "outForm/findUnRead";

    String ACCOUNT_BOOK = SERVER + "outForm/accountBook";

    String DETAIL_CANCEL = SERVER + "outForm/cancel";
}
