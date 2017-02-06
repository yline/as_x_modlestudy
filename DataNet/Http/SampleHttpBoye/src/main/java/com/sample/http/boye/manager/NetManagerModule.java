package com.sample.http.boye.manager;

/**
 * 没啥作用就是给NetManager 中做模板的
 */
public class NetManagerModule
{
	// b111 -> UserLogin   s222 -> userLogin  即可
	public void setb111Success()
	{
		if (null != s222Callback)
		{
			s222Callback.onSuccess();
		}
	}

	public void setb111NetError(String ex)
	{
		if (null != s222Callback)
		{
			s222Callback.onNetError(ex);
		}
	}

	public void setb111Error(String ex)
	{
		if (null != s222Callback)
		{
			s222Callback.onParamsError(ex);
		}
	}

	public void setOnb111Callback(Onb111Callback callback)
	{
		this.s222Callback = callback;
	}

	private Onb111Callback s222Callback;

	public interface Onb111Callback extends NetParamsError
	{
		void onSuccess();
	}
}
