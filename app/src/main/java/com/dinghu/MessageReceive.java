
package com.dinghu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 描述:用于监听网络变化和开机启动，主要任务是启动轮询推送消息
 *
 * @author jakechen
 * @since 2015/9/21 11:32
 */
public class MessageReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MessageService.class));
    }
}
