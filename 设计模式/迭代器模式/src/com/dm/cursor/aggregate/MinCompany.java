package com.dm.cursor.aggregate;

import java.util.ArrayList;
import java.util.List;

import com.dm.cursor.bean.Employee;
import com.dm.cursor.iterator.IIterator;
import com.dm.cursor.iterator.MinIterator;

public class MinCompany implements ICompany
{
    private List<Employee> listEE = new ArrayList<Employee>();
    
    // 初始化
    public MinCompany()
    {
        listEE.add(new Employee("xiao yi", 96, "nan", "chengxuyuan"));
        listEE.add(new Employee("xiao er", 96, "nan", "chengxuyuan"));
        listEE.add(new Employee("xiao san", 96, "nv", "chengxuyuan"));
        listEE.add(new Employee("xiao si", 96, "nan", "chengxuyuan"));
        listEE.add(new Employee("xiao wu", 96, "nan", "chengxuyuan"));
    }
    
    @Override
    public IIterator iIterator()
    {
        return new MinIterator(listEE);
    }
    
    public List<Employee> getMinCompany()
    {
        return listEE;
    }
}
