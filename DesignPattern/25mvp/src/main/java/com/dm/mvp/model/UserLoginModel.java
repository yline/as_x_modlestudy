package com.dm.mvp.model;

import com.dm.mvp.bean.UserLogin;
import com.dm.mvp.ilistener.OnUserLoginListener;

public class UserLoginModel implements IUserLoginModel
{
    
    public void login(final String username, final String password, final OnUserLoginListener loginListener)
    {
        //模拟子线程耗时操作
        new Thread()
        {
            
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                
                //模拟登录成功
                if ("yline".equals(username) && "123456".equals(password))
                {
                    UserLogin user = new UserLogin();
                    user.setUsername(username);
                    user.setPassword(password);
                    loginListener.loginSuccess(user);
                }
                else
                {
                    loginListener.loginFailed("登录失败原因");
                }
            }
        }.start();
    }
    
}
