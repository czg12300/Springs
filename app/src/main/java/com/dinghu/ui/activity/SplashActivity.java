
package com.dinghu.ui.activity;

import com.dinghu.R;
import com.dinghu.data.InitShareData;

import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;

import cn.common.ui.activity.BaseWorkerFragmentActivity;

/**
 * 启动页面
 */
public class SplashActivity extends BaseWorkerFragmentActivity {
    private ImageView mIvSplash;

    /**
     * 延时进入页面时间
     */
    private static final long DELAYED_TIME = 1 * 1000 - 500;

    /**
     * 进入引导页
     */
    private static final int MSG_GUIDE = 0;

    /**
     * 进入主页面
     */
    private static final int MSG_MAIN = 1;

    /**
     * 进入登录页面
     */
    private static final int MSG_LOGIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIvSplash = new ImageView(this);
        mIvSplash.setImageResource(R.drawable.splash);
        mIvSplash.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(mIvSplash);
        if (InitShareData.isLogin()) {
            sendEmptyUiMessageDelayed(MSG_MAIN, DELAYED_TIME);
        } else {
            sendEmptyUiMessageDelayed(MSG_LOGIN, DELAYED_TIME);
        }

    }

    @Override
    public void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_GUIDE:
                goActivity(GuideActivity.class);
                break;
            case MSG_LOGIN:
                goActivity(LoginActivity.class);
                break;
            case MSG_MAIN:
                goActivity(MainActivity.class);
                break;
        }
        finish();
    }
}
