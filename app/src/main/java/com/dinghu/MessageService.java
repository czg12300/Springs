
package com.dinghu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.AppConfig;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.MessageResponse;
import com.dinghu.ui.activity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述:
 *
 * @author jakechen
 * @since 2015/9/21 11:05
 */
public class MessageService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 点击查看
    private Intent messageIntent = null;

    private PendingIntent messagePendingIntent = null;

    // 通知栏消息
    private int messageNotificationID = 1000;

    private Notification messageNotification = null;

    private NotificationManager messageNotificationManager = null;

    private HandlerThread mHandlerThread;

    private Handler mBackgroundHandler;

    private BroadcastReceiver mReceiver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("message worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (TextUtils.equals(BroadcastActions.ACTION_EXIT_TO_LOGIN, action)) {
                    stopSelf();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastActions.ACTION_EXIT_TO_LOGIN);
        registerReceiver(mReceiver, filter);
        mBackgroundHandler = new Handler(mHandlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    HttpRequestManager<MessageResponse> request = new HttpRequestManager<MessageResponse>(
                            URLConfig.PUSH_MESSAGE, MessageResponse.class);
                    request.addParam("id", InitShareData.getUserId() + "");
                    MessageResponse response = request.sendRequest();
                    if (response != null && response.getCount() > 0) {
                        sendMessage2Notification("您有" + response.getCount() + "个配送任务");
                    }
                }
                return true;
            }
        });
        initNotification();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mBackgroundHandler.sendEmptyMessage(0);
            }
        }, 0, AppConfig.PUSH_MESSAGE_TIME_SPIT);

    }

    private void initNotification() {
        messageNotification = new Notification();
        messageNotification.icon = R.drawable.ic_launcher;
        messageNotification.tickerText = "配送提醒";
        messageNotification.flags = Notification.FLAG_AUTO_CANCEL;
        messageNotification.defaults = Notification.DEFAULT_SOUND;
        messageNotificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        messageIntent = new Intent(this, MainActivity.class);
        messageIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        messagePendingIntent = PendingIntent.getActivity(this, 0, messageIntent, 0);
    }

    private void sendMessage2Notification(String msg) {
        // 获取服务器消息
        if (!TextUtils.isEmpty(msg)) {
            // 更新通知栏
            messageNotification.setLatestEventInfo(MessageService.this, "配送提醒", msg,
                    messagePendingIntent);
            messageNotificationManager.notify(messageNotificationID, messageNotification);
            // 每次通知完，通知ID递增一下，避免消息覆盖掉
            messageNotificationID++;
        }
    }

}
