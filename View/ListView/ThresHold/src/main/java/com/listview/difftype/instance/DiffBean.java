package com.listview.difftype.instance;

/**
 * Created by yline on 2016/10/7.
 */
public class DiffBean
{
	private String userName;

	private String chatContent;

	private boolean isComMsg = true;

	public DiffBean(String userName, String chatContent, boolean isComMsg)
	{
		this.userName = userName;
		this.chatContent = chatContent;
		this.isComMsg = isComMsg;
	}
	
	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getChatContent()
	{
		return chatContent;
	}

	public void setChatContent(String chatContent)
	{
		this.chatContent = chatContent;
	}

	public boolean isComMsg()
	{
		return isComMsg;
	}

	public void setComMsg(boolean comMsg)
	{
		isComMsg = comMsg;
	}

	@Override
	public String toString()
	{
		return "DiffBean{" +
				"userName='" + userName + '\'' +
				", chatContent='" + chatContent + '\'' +
				", isComMsg=" + isComMsg +
				'}';
	}
}
