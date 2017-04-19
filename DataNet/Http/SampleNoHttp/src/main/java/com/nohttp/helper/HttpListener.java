package com.nohttp.helper;

import com.yanzhenjie.nohttp.rest.Response;

public interface HttpListener<T>
{
	void onSucceed(int what, Response<T> response);

	void onFailed(int what, Response<T> response);
}
