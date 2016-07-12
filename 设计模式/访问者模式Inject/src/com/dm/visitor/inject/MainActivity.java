package com.dm.visitor.inject;

import org.simple.injector.SimpleDagger;
import org.simple.injector.anno.ViewInjector;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yline.base.BaseFragmentActivity;

/**
 * 这个库运行错误,暂时先不考虑
 * 
 * 但是这个提供了一个APT的原理去理解注解方式
 * 
 * 注解开源库:ButterKnife,Dagger,Retrofit等等
 * 
 * 爱哥书的github：http://github.com/bboyfeiyu/InjectDagger
 * 
 * 刚刚开始会出错，要按照assets 目录下的图片所示操作一遍才有效果;
 * 之后作为jar去看
 */
public class MainActivity extends BaseFragmentActivity
{
    
    @ViewInjector(R.id.my_tv)
    protected TextView mTextView;
    
    @ViewInjector(R.id.my_tv2)
    protected TextView mTextView2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getSupportFragmentManager().beginTransaction().add(R.id.container, new TestFragment()).commit();
        
        SimpleDagger.inject(this);
        
        mTextView.setText("文字1");
        mTextView2.setText("文字2");
        
        if (mTextView != null)
        {
            Log.e("", "### my text view : " + mTextView.getText());
        }
    }
    
}
