package com.dm.observer.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.simple.eventbus.EventBus;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.yline.base.BaseFragmentActivity;

import dm.simple.eventbus.demo.bean.StickyUser;
import dm.simple.eventbus.demo.bean.User;
import dm.simple.eventbus.demo.fragment.ConstactFragment;
import dm.simple.eventbus.demo.fragment.MenuFragment;

public class MainActivity extends BaseFragmentActivity
{
    private FragmentManager fragmentManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .add(R.id.ll_fragment_menu, new MenuFragment())
            .add(R.id.ll_fragment_list, new ConstactFragment())
            .commit();
    }
    
    private void shiyongshouche()
    {
        // 注册一下
        EventBus.getDefault().register(this);
        
        // 发布事件
        EventBus.getDefault().postSticky(new StickyUser("我来自Sticky事件 - StickyUser类"));
        EventBus.getDefault().post(new User("Mr.Simple" + new Random().nextInt(100)));
        EventBus.getDefault().post(new User("User - 1"), "remove");
        EventBus.getDefault().post(new User("async-user"), "async");
        
        List<User> userLisr = new ArrayList<User>();
        for (int i = 0; i < 5; i++)
        {
            userLisr.add(new User("user - " + i));
        }
        EventBus.getDefault().post(userLisr);
        
        EventBus.getDefault().post(new User("super"), "super_tag");
        
        EventBus.getDefault().post("I am MainThread", "sub_thread");
        
        // 一个
        EventBus.getDefault().post(12345);
        // 整型数组
        EventBus.getDefault().post(new int[] {12, 24});
        EventBus.getDefault().post(true);
        
        // 在onDestroy中一定要调用
        EventBus.getDefault().unregister(this);
    }
}
