package com.dinghu.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.dinghu.SpringApplication;


/**
 * 存放初始化的值
 * Created by Administrator on 2015/8/15.
 */
public final class InitShareData {

    private InitShareData() {
    }

    /**
     * 文件名
     */
    private static final String FILE_NAME = "data";
    private static final String KEY_USER_ID = "keyUserId";
    private static final String KEY_MOBILE = "keyMobile";

    public static void setMobile(String mobile) {
        getSharedPreferences().edit().putString(KEY_MOBILE, mobile).commit();
    }

    public static String getMobile() {
        return getSharedPreferences().getString(KEY_MOBILE, null);
    }

    public static void setUserId(long id) {
        getSharedPreferences().edit().putLong(KEY_USER_ID, id).commit();
    }

    public static long getUserId() {
        return getSharedPreferences().getLong(KEY_USER_ID, -1);
    }

    public static boolean isLogin() {
        boolean isNotLogin = getUserId() < 0;
        return !isNotLogin;
    }

    /**
     * 获取sharePreference的编辑器
     *
     * @return
     */
    private static SharedPreferences getSharedPreferences() {
        return SpringApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }
}
