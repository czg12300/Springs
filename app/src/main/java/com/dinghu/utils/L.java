package com.dinghu.utils;

import android.text.TextUtils;
import android.util.Log;

import com.dinghu.logic.AppConfig;

/**
 * log
 */
public class L {

    private static boolean isOpenLog = AppConfig.IS_DEBUG; // true打开日志,false关闭日志

    private static final String LOG_NAME = "edg";

    public static void i(Object o) {
        if (!isOpenLog) {
            return;
        }
        if (null == o) {
            Log.i(LOG_NAME, "Object is null");
        } else {
            Log.i(LOG_NAME, o.toString());
        }
    }

    public static void i(String log) {
        if (!isOpenLog) {
            return;
        }
        if (TextUtils.isEmpty(log)) {
            log = log + "";
        }
        Log.i(LOG_NAME, log);
    }

    public static void i(String... strings) {
        if (!isOpenLog) {
            return;
        }
        if (strings != null && strings.length > 0) {
            String log;
            if (strings.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0, length = strings.length; i < length; i++) {
                    sb.append(strings[i]);
                }
                log = sb.toString();
                sb.reverse();
                sb = null;
            } else {
                log = strings[0];
            }
            if (!TextUtils.isEmpty(log)) {
                Log.i(LOG_NAME, log);
            }
        }
    }

    public static void i(String name, String log) {
        if (!isOpenLog) {
            return;
        }
        if (TextUtils.isEmpty(log)) {
            log = log + "";
        }
        Log.i(name, log);
    }

    public static void e(Class<?> c, String log) {
        if (!isOpenLog) {
            return;
        }
        if (TextUtils.isEmpty(log)) {
            log = log + "";
        }
        Log.e(c.getClass().getSimpleName(), log);
    }

    public static void e(String log) {
        if (!isOpenLog) {
            return;
        }
        if (TextUtils.isEmpty(log)) {
            log = log + "";
        }
        Log.e("error", log);
    }

    public static void w(String log) {
        if (!isOpenLog) {
            return;
        }
        if (TextUtils.isEmpty(log)) {
            log = log + "";
        }
        Log.w(LOG_NAME, log);
    }
}
