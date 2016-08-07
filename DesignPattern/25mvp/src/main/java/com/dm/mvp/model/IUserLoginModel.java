package com.dm.mvp.model;

import com.dm.mvp.ilistener.OnUserLoginListener;

public interface IUserLoginModel
{
    void login(String username, String password, OnUserLoginListener loginListener);
}
