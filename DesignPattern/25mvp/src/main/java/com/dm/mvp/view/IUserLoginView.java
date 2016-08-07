package com.dm.mvp.view;

import com.dm.mvp.bean.UserLogin;

public interface IUserLoginView
{
    /** 获取用户名 */
    String getUserName();
    
    /** 获取密码 */
    String getPassword();
    
    /** 登录成功 */
    void LoginSuccess(UserLogin userBean);
    
    /** 登录失败 */
    void LoginFailed(String error);
}
