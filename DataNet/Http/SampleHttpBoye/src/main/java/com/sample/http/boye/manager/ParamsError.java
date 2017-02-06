package com.sample.http.boye.manager;

/**
 * 返回的code错误,这个在NetError之后
 */
interface ParamsError
{
	/** 请求信息出错,例如缺失必要参数 */
	abstract void onParamsError(String ex);
}
