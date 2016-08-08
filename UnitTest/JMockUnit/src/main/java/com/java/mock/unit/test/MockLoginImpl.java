package com.java.mock.unit.test;

import com.java.mock.unit.Login;
import com.java.mock.unit.User;

public class MockLoginImpl implements Login
{
    
    @Override
    public User login(String username, String pwd)
    {
        return new User("123", username);
    }
}
