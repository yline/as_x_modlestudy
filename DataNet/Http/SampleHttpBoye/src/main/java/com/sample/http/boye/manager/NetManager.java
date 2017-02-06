package com.sample.http.boye.manager;


import com.sample.http.boye.bean.TokenBean;
import com.sample.http.boye.bean.UserLoginBean;

/**
 * http请求的callback
 */
public class NetManager
{
	private NetManager()
	{
	}

	/** 获取http请求实例 */
	public static NetManager getInstance()
	{
		return NetManagerHolder.sInstance;
	}

	private static class NetManagerHolder
	{
		private static final NetManager sInstance = new NetManager();
	}

	// token
	public void setTokenSuccess(TokenBean bean)
	{
		if (null != tokenCallback)
		{
			tokenCallback.onSuccess(bean);
		}
	}

	public void setTokenNetError(String ex)
	{
		if (null != tokenCallback)
		{
			tokenCallback.onNetError(ex);
		}
	}

	private OnTokenCallback tokenCallback;

	public void setOnTokenCallback(OnTokenCallback callback)
	{
		this.tokenCallback = callback;
	}

	public interface OnTokenCallback extends NetError
	{
		void onSuccess(TokenBean bean);
	}

	// 用户登录
	public void setUserLoginSuccess(UserLoginBean bean)
	{
		if (null != userLoginCallback)
		{
			userLoginCallback.onSuccess(bean);
		}
	}

	public void setUserLoginNetError(String ex)
	{
		if (null != userLoginCallback)
		{
			userLoginCallback.onNetError(ex);
		}
	}

	public void setUserLoginParamsError(String ex)
	{
		if (null != userLoginCallback)
		{
			userLoginCallback.onParamsError(ex);
		}
	}

	public void setOnUserLoginCallback(OnUserLoginCallback callback)
	{
		this.userLoginCallback = callback;
	}

	private OnUserLoginCallback userLoginCallback;

	public interface OnUserLoginCallback extends NetParamsError
	{
		void onSuccess(UserLoginBean bean);
	}

	// 图片上传	pictureUpload
	public void setPictureUploadSuccess()
	{
		if (null != pictureUploadCallback)
		{
			pictureUploadCallback.onSuccess();
		}
	}

	public void setPictureUploadNetError(String ex)
	{
		if (null != pictureUploadCallback)
		{
			pictureUploadCallback.onNetError(ex);
		}
	}

	public void setPictureUploadParamsError(String ex)
	{
		if (null != pictureUploadCallback)
		{
			pictureUploadCallback.onParamsError(ex);
		}
	}

	public void setOnPictureUploadCallback(OnPictureUploadCallback callback)
	{
		this.pictureUploadCallback = callback;
	}

	private OnPictureUploadCallback pictureUploadCallback;

	public interface OnPictureUploadCallback extends NetParamsError
	{
		void onSuccess();
	}
}
