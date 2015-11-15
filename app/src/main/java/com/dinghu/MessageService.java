
package com.dinghu;

import com.dinghu.data.BroadcastActions;
import com.dinghu.data.InitShareData;
import com.dinghu.logic.AppConfig;
import com.dinghu.logic.URLConfig;
import com.dinghu.logic.http.HttpRequestManager;
import com.dinghu.logic.http.response.MessageResponse;
import com.dinghu.ui.activity.MainActivity;

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
                        Intent it=new Intent("com.dinghu.MessageHandleReceive.ACTION");
                        it.putExtra("Message",response.getCount());
                        sendBroadcast(it);
                    }
                }
                return true;
            }
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mBackgroundHandler.sendEmptyMessage(0);
            }
        }, 0, AppConfig.PUSH_MESSAGE_TIME_SPIT);

    }


}
