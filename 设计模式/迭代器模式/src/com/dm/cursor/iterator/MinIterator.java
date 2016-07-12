package com.dm.cursor.iterator;

import java.util.List;

import com.dm.cursor.bean.Employee;

public class MinIterator implements IIterator
{
    private List<Employee> list;
    
    private int position;
    
    public MinIterator(List<Employee> list)
    {
        this.list = list;
    }
    
    @Override
    public boolean hasNext()
    {
        return !(position > list.size() - 1 || list.get(position) == null);
    }
    
    @Override
    public Object next()
    {
        Employee ee = list.get(position);
        position++;
        return ee;
    }
    
}
