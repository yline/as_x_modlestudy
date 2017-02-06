package com.sample.http.boye.obtain;

import android.util.Base64;

import com.google.gson.Gson;
import com.sample.http.boye.activity.MainApplication;
import com.sample.http.boye.bean.PublicBean;
import com.sample.http.boye.bean.TokenBean;
import com.sample.http.boye.constants.NetConstant;
import com.sample.http.boye.constants.SPHttpConstant;
import com.sample.http.boye.decode.NetToken;
import com.sample.http.boye.manager.NetManager;
import com.yline.log.LogFileUtil;
import com.yline.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * 有token,有固定格式,数据量较小
 */
public class NetPublicObtain
{
	public static final String API_VER_VALUE = "100";

	private NetToken token = new NetToken();

	// 获取网络数据
	public void getData(final Map<?, ?> map, final String type, final String api_ver)
	{
		if (isTokenTimeOut())
		{
			token.getToken();
			NetManager.getInstance().setOnTokenCallback(new NetManager.OnTokenCallback()
			{

				@Override
				public void onNetError(String ex)
				{
					errorNet(ex);
				}

				@Override
				public void onSuccess(TokenBean bean)
				{
					SPUtil.put(x.app(), SPHttpConstant.EXPIRES_IN, bean.getExpires_in());
					SPUtil.put(x.app(), SPHttpConstant.ACCESS_TOKEN, bean.getAccess_token());
					SPUtil.put(x.app(), SPHttpConstant.TIME_STAMP, getTimeStamp());

					getPublicData(map, type, bean.getAccess_token(), api_ver);
				}
			});
		}
		else
		{ // 未超时
			String token_value = (String) SPUtil.get(x.app(), SPHttpConstant.ACCESS_TOKEN, "");
			getPublicData(map, type, token_value, api_ver);
		}
	}

	private void getPublicData(Map<?, ?> map, String type, String token_value, String api_ver)
	{
		String sTime = getTimeStamp() + "";

		String data = dataEncode(map);
		String sign;
		try
		{
			sign = getMD5Sign(sTime, type, data, CHolder.CLIENT_ID_VALUE, CHolder.NOTIFY_ID_VALUE);
		}
		catch (NoSuchAlgorithmException e)
		{
			errorParams("数字签名不能生成");
			e.printStackTrace();
			return;
		}

		RequestParams params = new RequestParams(NetConstant.BASE_HTTP); // 外网地址
		// 头部参数
		params.addBodyParameter(CHolder.GRANT_TYPE_KEY, CHolder.GRANT_TYPE_VALUE);
		params.addBodyParameter(CHolder.CLIENT_ID_KEY, CHolder.CLIENT_ID_VALUE);
		params.addBodyParameter(CHolder.CLIENT_SECRET_KEY, CHolder.CLIENT_SECRET_VALUE);
		params.addBodyParameter(CHolder.ACCESS_TOKEN_KEY, token_value);
		// 基本公共参数(6个)
		params.addParameter(CHolder.TIME_KEY, sTime);
		params.addBodyParameter(CHolder.SIGN_KEY, sign);
		params.addBodyParameter(CHolder.ALG_KEY, CHolder.ALG_VALUE);
		params.addBodyParameter(CHolder.NOTIFY_ID_KEY, CHolder.NOTIFY_ID_VALUE);
		params.addBodyParameter(CHolder.TYPE_KEY, type);
		params.addBodyParameter(CHolder.API_VER_KEY, api_ver);
		// 数据
		params.addBodyParameter(CHolder.DATA_KEY, data);

		x.http().post(params, new Callback.CommonCallback<String>()
		{

			@Override
			public void onCancelled(CancelledException cex)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback)
			{
				errorNet(ex + "");
			}

			@Override
			public void onFinished()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String result)
			{
				Gson gson = new Gson();
				PublicBean bean = gson.fromJson(result, PublicBean.class);
				if (0 == bean.getCode())
				{
					String jsonData = dataDecode(bean.getData());

					try
					{
						JSONObject jsonObject = new JSONObject(jsonData);
						int code = jsonObject.getInt("code");
						String data = jsonObject.getString("data");
						if (0 == code)
						{
							success(data); // 解析正确
						}
						else
						{
							errorParams(data); // 参数错误
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						errorNet(e + "");
					}
				}
				else
				{
					errorParams("数据格式错误或令牌失效");
				}
			}
		});
	}

	/**
	 * 判断token是否超时
	 * @return true 超时
	 */
	private boolean isTokenTimeOut()
	{
		long oldTime = (Long) SPUtil.get(x.app(), SPHttpConstant.TIME_STAMP, 1449200000L);
		long newTime = getTimeStamp();
		int limitTime = (Integer) SPUtil.get(x.app(), SPHttpConstant.EXPIRES_IN, 60);
		if (newTime - oldTime > limitTime - 60)
		{ // 给个缓冲时间60s
			LogFileUtil.v(MainApplication.TAG, "令牌超时,重新获取");
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 再次解析
	 * @param data 已初步解析的data信息
	 */
	protected void success(String data)
	{
		LogFileUtil.v(MainApplication.TAG, "网络返回数据： " + data);
	}

	/**
	 * 网络错误+Json格式错误(网络错误导致的)
	 * @param ex 错误提示
	 */
	protected void errorNet(String ex)
	{
		LogFileUtil.v(MainApplication.TAG, "网络错误： " + ex);
	}

	/**
	 * 请求参数错误
	 * @param ex 错误提示
	 */
	protected void errorParams(String ex)
	{
		LogFileUtil.v(MainApplication.TAG, "请求参数错误： " + ex);
	}

	private long getTimeStamp()
	{
		long time = System.currentTimeMillis() / 1000;
		return time;
	}

	/**
	 * 加密
	 * @param data_array map集合
	 * @return json字符串后的加密信息
	 */
	private String dataEncode(Map<?, ?> data_array)
	{
		Gson gson = new Gson();
		String jsonStr = gson.toJson(data_array);
		LogFileUtil.v(MainApplication.TAG, "提交信息：" + jsonStr);

		// 加密
		String Base64Str = new String(Base64.encode(jsonStr.getBytes(), Base64.DEFAULT));
		String data = new String(Base64.encode(Base64Str.getBytes(), Base64.DEFAULT));

		return data;
	}

	/**
	 * 数据解密
	 * @param str 加密字符串
	 * @return 解密后的字符串
	 */
	private String dataDecode(String str)
	{
		String Base64Str = new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
		String jsonStr = new String(Base64.decode(Base64Str.getBytes(), Base64.DEFAULT));
		return jsonStr;
	}

	/**
	 * @param time      时间戳
	 * @param type      文档的type	BY_User_login
	 * @param data      json数据
	 * @param client_id 给管理员有的	by565fa4e56a9241
	 * @param notify_id 随意			111
	 * @return sign 签名
	 */
	private String getMD5Sign(String time, String type, String data, String client_id, String notify_id)
			throws NoSuchAlgorithmException
	{
		String sign = getMD5(time + type + data + client_id + notify_id);
		return sign;
	}

	/**
	 * MD5加密
	 * @param value
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String getMD5(String sourceStr)
			throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(sourceStr.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++)
		{
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		String result = buf.toString();
		//	System.out.println("result: " + result);	//32位的加密
		//  System.out.println("result: " + buf.toString().substring(8,24));//16位的加密
		return result;
	}

	private class CHolder
	{
		// 授权类型(固定为client_credentials)
		private static final String GRANT_TYPE_KEY = "grant_type";

		private static final String GRANT_TYPE_VALUE = "client_credentials";

		// 客户端ID
		private static final String CLIENT_ID_KEY = "client_id";

		private static final String CLIENT_ID_VALUE = "by565fa4facdb191";

		// 客户端密钥
		private static final String CLIENT_SECRET_KEY = "client_secret";

		private static final String CLIENT_SECRET_VALUE = "b6b27d3182d589b92424cac0f2876fcd";

		// token 网络请求 令牌
		private static final String ACCESS_TOKEN_KEY = "access_token";

		// 请求时间 时间戳
		private static final String TIME_KEY = "time";

		// 签名，客服端对传输数据的签名结果
		private static final String SIGN_KEY = "sign";

		// 目前固定为md5 签名方式
		private static final String ALG_KEY = "alg";

		private static final String ALG_VALUE = "md5";

		// 请求的id，用来标识请求(会原样返回)
		private static final String NOTIFY_ID_KEY = "notify_id";

		private static final String NOTIFY_ID_VALUE = "111";

		// 文档的type BY_User_login
		private static final String TYPE_KEY = "type";

		// 请求接口的版本
		private static final String API_VER_KEY = "api_ver";

		// 请求的数据key
		private static final String DATA_KEY = "data";
	}
}
