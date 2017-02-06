package com.sample.http.boye.bean;

public class TokenBean
{
	/**
	 * Token 令牌
	 */
	private String access_token;

	/**
	 * 网络超时时间
	 */
	private int expires_in;

	private String token_type;

	private String scope;

	public String getAccess_token()
	{
		return access_token;
	}

	public void setAccess_token(String access_token)
	{
		this.access_token = access_token;
	}

	public int getExpires_in()
	{
		return expires_in;
	}

	public void setExpires_in(int expires_in)
	{
		this.expires_in = expires_in;
	}

	public String getToken_type()
	{
		return token_type;
	}

	public void setToken_type(String token_type)
	{
		this.token_type = token_type;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}
}
