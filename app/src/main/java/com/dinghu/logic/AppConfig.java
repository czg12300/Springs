
package com.dinghu.logic;

/**
 * 描述：所有的配置信息
 *
 * @author jake
 * @since 2015/9/19 16:48
 */
public class AppConfig {
    public static final boolean IS_DEBUG = false;

    public static final int PUSH_MESSAGE_TIME_SPIT_TEST = 10 * 1000;

    public static final int PUSH_MESSAGE_TIME_SPIT_RELEASE = 3 * 60 * 1000;

    public static final int PUSH_MESSAGE_TIME_SPIT = IS_DEBUG ? PUSH_MESSAGE_TIME_SPIT_TEST
            : PUSH_MESSAGE_TIME_SPIT_RELEASE;
}
