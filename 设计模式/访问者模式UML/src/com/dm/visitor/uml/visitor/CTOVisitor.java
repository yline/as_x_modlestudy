package com.dm.visitor.uml.visitor;

import com.dm.visitor.uml.activity.MainApplication;
import com.dm.visitor.uml.element.Engineer;
import com.dm.visitor.uml.element.Manager;
import com.yline.log.LogFileUtil;


public class CTOVisitor implements Visitor
{
    
    @Override
    public void visit(Engineer engineer)
    {
        LogFileUtil.v(MainApplication.TAG, "工程师:" + engineer.name + ",代码函数:" + engineer.getCodeLines());
    }
    
    @Override
    public void visit(Manager manager)
    {
        LogFileUtil.v(MainApplication.TAG, "经理:" + manager.name + ",产品数量:" + manager.getProducts());
    }
    
}
