package com.dm.mvp.presenter;

import android.os.Handler;

import com.dm.mvp.bean.UserLogin;
import com.dm.mvp.ilistener.OnUserLoginListener;
import com.dm.mvp.model.IUserLoginModel;
import com.dm.mvp.model.UserLoginModel;
import com.dm.mvp.view.IUserLoginView;

public class UserLoginPresenter
{
    private IUserLoginModel userBiz;
    
    private IUserLoginView userLoginView;
    
    private Handler mHandler = new Handler();
    
    public UserLoginPresenter(IUserLoginView userLoginView)
    {
        this.userLoginView = userLoginView;
        this.userBiz = new UserLoginModel();
    }
    
    public void login()
    {
        userBiz.login(userLoginView.getUserName(), userLoginView.getPassword(), new OnUserLoginListener()
        {
            
            @Override
            public void loginSuccess(final UserLogin user)
            {
                //需要在UI线程执行
                mHandler.post(new Runnable()
                {
                    
                    @Override
                    public void run()
                    {
                        userLoginView.LoginSuccess(user);
                    }
                });
                
            }
            
            @Override
            public void loginFailed(final String ex)
            {
                //需要在UI线程执行
                mHandler.post(new Runnable()
                {
                    
                    @Override
                    public void run()
                    {
                        userLoginView.LoginFailed(ex); // 这里可以加原因
                    }
                });
                
            }
        });
    }
}
