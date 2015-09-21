
package com.dinghu;

import android.content.Intent;

import cn.common.ui.activity.BaseApplication;

/**
 * @author Created by jakechen on 2015/8/6.
 */
public class SpringApplication extends BaseApplication {

    @Override
    protected void onConfig() {
        startService(new Intent(this, MessageService.class));
    }

    @Override
    protected void onRelease() {
    }

    @Override
    protected BaseApplication getChildInstance() {
        return this;
    }
}
