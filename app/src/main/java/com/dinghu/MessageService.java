package com.dinghu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

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

  @Override
  public void onCreate() {
    super.onCreate();
    mHandlerThread = new HandlerThread("message worker:" + getClass().getSimpleName());
    mHandlerThread.start();
    mBackgroundHandler = new Handler(mHandlerThread.getLooper(), new Handler.Callback() {
      @Override
      public boolean handleMessage(Message msg) {
        if (msg.what == 0) {
//          HttpRequestManager<MessageResponse> request = new HttpRequestManager<MessageResponse>(URLConfig.SERVER, MessageResponse.class);
//          MessageResponse response = request.sendRequest();
//          if (response.isOk() && response.getCode() == MessageResponse.CODE_SUCCESS) {
//            sendMessage2Notifiction("您有" + response.getMsg() + "个配送任务");
//          }
          sendMessage2Notification("您有" + 1 + "个z新的配送任务");

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
    }, 0, 1 * 60 * 1000);

  }

  private void initNotification() {
    messageNotification = new Notification();
    messageNotification.icon = R.drawable.ic_launcher;
    messageNotification.tickerText = "配送提醒";
    messageNotification.defaults = Notification.DEFAULT_SOUND;
    messageNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    messageIntent = new Intent(this, MainActivity.class);
    messagePendingIntent = PendingIntent.getActivity(this, 0, messageIntent, 0);
  }

  private void sendMessage2Notification(String msg) {
    // 获取服务器消息
    if (!TextUtils.isEmpty(msg)) {
      // 更新通知栏
      messageNotification.setLatestEventInfo(MessageService.this, "新消息", msg, messagePendingIntent);
      messageNotificationManager.notify(messageNotificationID, messageNotification);
      // 每次通知完，通知ID递增一下，避免消息覆盖掉
      messageNotificationID++;
    }
  }
}
