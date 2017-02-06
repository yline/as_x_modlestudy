package com.sample.http.boye.manager;

/**
 * 网络错误+请求参数错误
 */
interface NetParamsError
{
	/** 网络错误+解析出错(网络原因导致非预期格式造成) */
	abstract void onNetError(String ex);

	/** 请求信息出错,例如缺失必要参数 */
	abstract void onParamsError(String ex);
}
