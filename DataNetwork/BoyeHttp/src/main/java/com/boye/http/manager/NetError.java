package com.boye.http.manager;

/**
 * 网络请求错误,一般Json解析错误也在此列(因为,网络错误后,会导致json格式不是预期格式,因此出错)
 */
interface NetError
{
    /** 网络错误+解析出错(网络原因导致非预期格式造成) */
    abstract void onNetError(String ex);
}
