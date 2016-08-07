package com.dm.visitor.uml.visitor;

import com.dm.visitor.uml.element.Engineer;
import com.dm.visitor.uml.element.Manager;

public interface Visitor
{
    // 访问工程师类型
    public void visit(Engineer engineer);
    
    // 访问经理类型
    public void visit(Manager manager);
}
