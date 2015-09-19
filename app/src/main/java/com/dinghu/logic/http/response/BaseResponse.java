package com.dinghu.logic.http.response;

import cn.common.bitmap.utils.L;
import cn.common.http.JsonParse;

/**
 * 描述：所有请求结果的父类
 *
 * @author jake
 * @since 2015/9/19 23:36
 */
public abstract class BaseResponse implements JsonParse {
    private boolean isOk;

    public boolean isOk() {
        return isOk;
    }

    public void setIsOk(boolean isOk) {
        this.isOk = isOk;
    }
}
