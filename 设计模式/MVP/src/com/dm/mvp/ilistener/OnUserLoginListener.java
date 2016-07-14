package com.dm.mvp.ilistener;

import com.dm.mvp.bean.UserLogin;

public interface OnUserLoginListener
{
    void loginSuccess(UserLogin user);
    
    void loginFailed(String ex);
}
