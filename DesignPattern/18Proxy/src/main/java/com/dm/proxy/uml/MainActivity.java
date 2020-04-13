package com.dm.proxy.uml;

import android.os.Bundle;
import android.view.View;

import com.design.pattern.proxy.R;
import com.dm.proxy.uml.proxysubject.LawyerInvocationHandler;
import com.dm.proxy.uml.proxysubject.Lawyer;
import com.dm.proxy.uml.realsubject.XiaoMin;
import com.dm.proxy.uml.subject.ILawsuit;
import com.yline.test.BaseTestActivity;

import java.lang.reflect.Proxy;

/**
 * 动态代理: 通过一个代理类来替代 多个被替代类,实质是对代理者与被代理者进行解耦,使两者之间没有直接的耦合
 * 静态代理: 只是为给定接口下的实现类做代理
 * <p/>
 * 它们两个依据code方面分为两种模式:
 * 远程代理: 为某个对象在不同的内存地址空间提供局部管理。使系统可以将Server部分的部分隐藏，以便Client可以不必考虑Server的存在
 * 虚拟代理: 使用一个代理对象表示一个十分耗资源的对象并在真正需要时才创建
 * 保护代理: 使用代理控制对原始对象的访问,该类型的代理常被用于原始对象有不同访问权限的情况
 * 智能引用: 在访问原始对象时执行一些自己的附加操作,并对指向原始对象的引用计数
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        // 静态代理
        addButton("static proxy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ILawsuit xiaomin = new XiaoMin();
                ILawsuit lawyer = new Lawyer(xiaomin);

                lawyer.submit();
                lawyer.burden();
                lawyer.defend();
                lawyer.finish();
            }
        });

        // 动态代理,这样就可以实现,不同的xiaomin采用相同的操作;这里的确就是单一的拿到自身的代理
        addButton("dynamic proxy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ILawsuit xiaomin = new XiaoMin();

                // 构造一个动态代理,这里传入什么,之后构造出来就是什么
                ILawsuit lawyer = (ILawsuit) LawyerInvocationHandler.newProxy(xiaomin, ILawsuit.class);
                lawyer.submit();
                lawyer.burden();
                lawyer.defend();
                lawyer.finish();
            }
        });
    }


}
