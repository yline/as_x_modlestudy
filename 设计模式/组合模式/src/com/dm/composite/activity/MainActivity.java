package com.dm.composite.activity;

import android.os.Bundle;
import android.view.View;

import com.dm.composite.R;
import com.dm.composite.safe.SComponent;
import com.dm.composite.safe.SComposite;
import com.dm.composite.safe.SLeaf;
import com.dm.composite.transport.TComponent;
import com.dm.composite.transport.TComposite;
import com.dm.composite.transport.TLeaf;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

public class MainActivity extends BaseActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_safe).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_safe");
                
                // 根节点
                SComposite rootComposite = new SComposite("root");
                
                // 枝干节点
                SComposite branchCompositeA = new SComposite("branchA");
                SComposite branchCompositeB = new SComposite("branchB");
                
                // 叶子节点
                SComponent leafA = new SLeaf("leafA");
                SComponent leafB = new SLeaf("leafB");
                
                branchCompositeA.addChild(leafA);
                branchCompositeB.addChild(leafB);
                
                rootComposite.addChild(branchCompositeA);
                rootComposite.addChild(branchCompositeB);
                
                rootComposite.doSomething();
            }
        });
        
        findViewById(R.id.btn_transport).setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                LogFileUtil.v(MainApplication.TAG, "onClick -> btn_transport");
                
                // 根节点
                TComponent rootComposite = new TComposite("root");
                
                // 枝干节点
                TComponent branchCompositeA = new TComposite("branchA");
                TComponent branchCompositeB = new TComposite("branchB");
                
                // 叶子节点
                TComponent leafA = new TLeaf("leafA");
                TComponent leafB = new TLeaf("leafB");
                
                branchCompositeA.addChild(leafA);
                branchCompositeB.addChild(leafB);
                
                rootComposite.addChild(branchCompositeA);
                rootComposite.addChild(branchCompositeB);
                
                rootComposite.doSomething();
            }
        });
    }
    
}
