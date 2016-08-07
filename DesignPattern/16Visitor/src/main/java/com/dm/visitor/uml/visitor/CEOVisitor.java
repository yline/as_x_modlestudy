package com.dm.visitor.uml.visitor;

import com.dm.visitor.uml.activity.MainApplication;
import com.dm.visitor.uml.element.Engineer;
import com.dm.visitor.uml.element.Manager;
import com.yline.log.LogFileUtil;


public class CEOVisitor implements Visitor
{
    
    @Override
    public void visit(Engineer engineer)
    {
        LogFileUtil.v(MainApplication.TAG, "工程师:" + engineer.name + ",kpi:" + engineer.kpi);
    }
    
    @Override
    public void visit(Manager manager)
    {
        LogFileUtil.v(MainApplication.TAG, "经理:" + manager.name + ",kpi:" + manager.kpi);
    }
    
}
