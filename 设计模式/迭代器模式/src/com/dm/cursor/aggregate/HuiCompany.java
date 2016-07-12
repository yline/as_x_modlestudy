package com.dm.cursor.aggregate;

import com.dm.cursor.bean.Employee;
import com.dm.cursor.iterator.HuiIterator;
import com.dm.cursor.iterator.IIterator;

public class HuiCompany implements ICompany
{
    private Employee[] array = new Employee[3];
    
    public HuiCompany()
    {
        array[0] = new Employee("yi", 108, "nv", "shejishi");
        array[1] = new Employee("er", 108, "nv", "shejishi");
        array[2] = new Employee("sa", 108, "nv", "shejishi");
    }
    
    public Employee[] getHuiCompany()
    {
        return array;
    }
    
    @Override
    public IIterator iIterator()
    {
        return new HuiIterator(array);
    }
    
}
