package com.dinghu.logic.http;

import android.text.TextUtils;

import com.dinghu.logic.URLConfig;
import com.dinghu.utils.MD5Util;

import java.util.HashMap;

import cn.common.http.AjaxParams;
import cn.common.http.HttpManager;
import cn.common.utils.CommonUtil;
import cn.common.utils.ThreadPoolUtil;

public class HttpRequestManager<T> {
    private String mSvc;
    private HashMap<String, String> mParams;
    private Class<?> mClazz;
    private HttpManager<T> mHttpManager;

    public HttpRequestManager(String svc, Class<?> clazz) {
        mSvc = svc;
        mClazz = clazz;
        mParams = new HashMap<String, String>();
        addParam("version", "" + CommonUtil.getAppVersion());
        addParam("dinghu_key", MD5Util.md5("@230_dinghuKey_dinghu.cn_Saura_fdssda"));
        mHttpManager = new HttpManager<T>(mClazz);
    }

    public void addParam(String key, String value) {
        mParams.put(key, value);
    }

    public void addParams(HashMap<String, String> params) {
        if (params != null && params.size() > 0) {
            mParams.putAll(params);
        }
    }

    public void setParams(HashMap<String, String> params) {
        if (params != null && params.size() > 0) {
            mParams = params;
        }
    }

    /**
     * 开始请求
     */
    public T sendRequest() {
        return mHttpManager.post(getUrl(), getParams());
    }

    private String getUrl() {
        if (!TextUtils.isEmpty(mSvc) && mSvc.startsWith("http")) {
            return mSvc;
        }
        return URLConfig.SERVER + mSvc;
    }

    private AjaxParams getParams() {
        return new AjaxParams(mParams);
    }

    /**
     * 取消请求
     */
    public synchronized void cancelRequest() {
        mHttpManager.cancelRequest();
    }
}
