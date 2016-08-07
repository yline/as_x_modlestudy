package com.dm.visitor.uml.structure;

import java.util.LinkedList;
import java.util.List;

import com.dm.visitor.uml.element.Engineer;
import com.dm.visitor.uml.element.Manager;
import com.dm.visitor.uml.element.Staff;
import com.dm.visitor.uml.visitor.Visitor;


/**
 * 定义当中所提到的对象结构,对象结构是一个抽象表述,它内部管理了元素集合,并且可以迭代这些元素供访问者访问
 */
public class BusinessReport
{
    // 双向列表,列表中的每个节点都包含了对前一个和后一个元素的引用
    private List<Staff> mStaffs = new LinkedList<Staff>();
    
    public BusinessReport()
    {
        mStaffs.add(new Manager("namager yi"));
        mStaffs.add(new Engineer("engineer yi"));
        mStaffs.add(new Manager("namager er"));
        mStaffs.add(new Engineer("engineer er"));
        mStaffs.add(new Manager("namager san"));
        mStaffs.add(new Engineer("engineer san"));
        mStaffs.add(new Manager("namager si"));
        mStaffs.add(new Engineer("engineer si"));
        mStaffs.add(new Engineer("engineer wu"));
        mStaffs.add(new Manager("namager wu"));
        mStaffs.add(new Engineer("engineer liu"));
    }
    
    public void showReport(Visitor visitor)
    {
        for (Staff staff : mStaffs)
        {
            staff.accept(visitor);
        }
    }
}
