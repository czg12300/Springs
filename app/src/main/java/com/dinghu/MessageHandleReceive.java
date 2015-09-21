
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

    // 通知栏消息
    private int messageNotificationID = 1000;

    private Notification messageNotification = null;

    private NotificationManager messageNotificationManager = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), ACTION)) {
            MessageResponse response = (MessageResponse) intent.getSerializableExtra("Message");
            // 初始化
            messageNotification = new Notification();
            messageNotification.icon = R.drawable.ic_launcher;
            messageNotification.tickerText = "新消息";
            messageNotification.defaults = Notification.DEFAULT_SOUND;
            messageNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            // 点击查看
            Intent messageIntent = new Intent(context, MainActivity.class);
            PendingIntent messagePendingIntent = PendingIntent.getActivity(context, 0,
                    messageIntent, 0);
        }
    }
}
