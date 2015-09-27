package com.dinghu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.common.utils.CommonUtil;

/**
 * 描述：
 *
 * @author jake
 * @since 2015/9/27 22:54
 */
public class Utils extends CommonUtil {
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }
}
