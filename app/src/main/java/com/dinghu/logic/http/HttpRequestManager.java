
package com.dinghu.logic.http;

import com.dinghu.logic.URLConfig;
import com.dinghu.utils.MD5Util;

import cn.common.http.base.BaseRequest;
import cn.common.utils.CommonUtil;

public class HttpRequestManager<T> extends BaseRequest {

    public HttpRequestManager(String svc, Class<?> clazz) {
        super(svc, clazz);
    }

    @Override
    protected void addCommonParam() {
        addParam("version", "" + CommonUtil.getAppVersion());
        addParam("dinghu_key", MD5Util.md5("@230_dinghuKey_dinghu.cn_Saura_fdssda"));
    }

    @Override
    protected String getServerUrl() {
        return URLConfig.SERVER;
    }

}
