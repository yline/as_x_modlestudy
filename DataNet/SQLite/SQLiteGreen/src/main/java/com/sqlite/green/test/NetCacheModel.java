package com.sqlite.green.test;

import java.io.Serializable;

/**
 * 基础，网络请求的缓存表
 *
 * @author yline 2017/9/14 -- 10:00
 * @version 1.0.0
 */
public class NetCacheModel implements Serializable{
    public static final String NetCacheSplitStr = "%s$#%s";

    private String requestUrl; // http请求的Url + 参数(自己拼接)

    private String requestTag; // http请求的 tag

    private byte[] resultHeader; // 返回的头部，可以为空

    private byte[] resultData; // 返回的数据

    /**
     * 数据库缓存结构体；适合相同URL，对应相同内容的情况
     *
     * @param requestUrl Url
     * @param resultData 返回的数据
     */
    public NetCacheModel(String requestUrl, byte[] resultData) {
        this.requestUrl = requestUrl;
        this.resultData = resultData;
    }

    /**
     * 数据库缓存结构体；适合相同URL，对应不同内容的情况
     *
     * @param requestUrl   Url
     * @param requestParam 传入的参数
     * @param resultData   返回数据
     */
    public NetCacheModel(String requestUrl, String requestParam, byte[] resultData) {
        this(String.format(NetCacheSplitStr, requestUrl, requestParam), resultData);
    }

    public NetCacheModel(String requestUrl, String requestTag, byte[] resultHeader, byte[] resultData) {
        this.requestUrl = requestUrl;
        this.requestTag = requestTag;
        this.resultHeader = resultHeader;
        this.resultData = resultData;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(String requestTag) {
        this.requestTag = requestTag;
    }

    public byte[] getResultHeader() {
        return resultHeader;
    }

    public void setResultHeader(byte[] resultHeader) {
        this.resultHeader = resultHeader;
    }

    public byte[] getResultData() {
        return resultData;
    }

    public void setResultData(byte[] resultData) {
        this.resultData = resultData;
    }
}
