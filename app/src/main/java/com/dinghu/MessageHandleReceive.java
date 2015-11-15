
package com.dinghu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dinghu.logic.http.response.MessageResponse;
import com.dinghu.ui.activity.MainActivity;

/**
 * 描述:处理消息推送的广播
 *
 * @author jakechen
 * @since 2015/9/21 11:54
 */
public class MessageHandleReceive extends BroadcastReceiver {
    public static final String ACTION = "com.dinghu.MessageHandleReceive.ACTION";
    // 点击查看
    private Intent messageIntent = null;

    private PendingIntent messagePendingIntent = null;

    // 通知栏消息
    private int messageNotificationID = 1000;

    private Notification messageNotification = null;

    private NotificationManager messageNotificationManager = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), ACTION) && intent.hasExtra("Message")) {
            int count = intent.getIntExtra("Message", 0);
            if (count > 0) {
                sendMessage2Notification(context, "您有" + count + "个配送任务");
            }
        }
    }

    private void initNotification(Context context) {
        messageNotification = new Notification();
        messageNotification.icon = R.drawable.ic_launcher;
        messageNotification.tickerText = "配送提醒";
        messageNotification.flags = Notification.FLAG_AUTO_CANCEL;
        messageNotification.defaults = Notification.DEFAULT_SOUND;
        messageNotificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        messageIntent = new Intent(context, MainActivity.class);
        messageIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        messageIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        messagePendingIntent = PendingIntent.getActivity(context, 0, messageIntent, 0);
    }

    private void sendMessage2Notification(Context context, String msg) {
        if (context == null || TextUtils.isEmpty(msg)) {
            return;
        }
        if (messageNotification == null) {
            initNotification(context);
        }
        // 获取服务器消息
        // 更新通知栏
        messageNotification.setLatestEventInfo(context, "配送提醒", msg,
                messagePendingIntent);
        messageNotificationManager.notify(messageNotificationID, messageNotification);
        // 每次通知完，通知ID递增一下，避免消息覆盖掉
        messageNotificationID++;
    }
}
