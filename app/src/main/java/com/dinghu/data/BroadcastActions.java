
package com.dinghu.data;

/**
 * 描述：用于存放广播信息 Created by jakechen on 2015/8/27.
 */
public interface BroadcastActions {
    /**
     * 更新todo的列表
     */
    String ACTION_UPDATE_TODO_WORK_LIST = "com.dinghu.data.BroadcastActions.action_update_todo_work_list";

    /**
     * 更新今日完工页面
     */
    String ACTION_UPDATE_TODAY_WORK_LIST = "com.dinghu.data.BroadcastActions.action_update_today_work_list";

    /**
     * 更新历史完工页面
     */
    String ACTION_UPDATE_HISTORY_WORK_LIST = "com.dinghu.data.BroadcastActions.action_update_history_work_list";

    String ACTION_EXIT_TO_LOGIN = "com.dinghu.data.BroadcastActions.action_exit_to_login";

    /**
     * 改变显示模式
     */
    String ACTION_CHANGE_MODE = "com.dinghu.data.BroadcastActions.ACTION_CHANGE_MODE";

    /**
     * 修改头像
     */
    String ACTION_FINISH_USER_INFO_AVATOR = "cn.protector.data.BroadcastActions.action_finish_user_info_avator";

    String ACTION_FINISH_ACITIVTY_BEFORE_MAIN = "cn.protector.data.BroadcastActions.action_finish_acitivty_beforemain";

    /**
     * 选择主页的定位tab
     */
    String ACTION_MAIN_ACTIVITY_SELECT_TAB_LOCATE = "cn.protector.data.BroadcastActions.action_main_activity_select_tab_locate";

    /**
     * 选择不同的设备
     */
    String ACTION_MAIN_DEVICE_CHANGE = "cn.protector.data.BroadcastActions.action_main_device_change";

    /**
     * 获取所有设备信息
     */
    String ACTION_GET_ALL_DEVICES = "cn.protector.data.BroadcastActions.action_get_all_devices";

}
