package com.dm.cursor.iterator;

import com.dm.cursor.bean.Employee;

public class HuiIterator implements IIterator
{
    private Employee[] arrayEE;
    
    private int position; // 在未指定值的时候,默认 = 0;
    
    public HuiIterator(Employee[] arrayEE)
    {
        this.arrayEE = arrayEE;
    }
    
    /**
     * 理解为,第一个数据就是null 的前一个
     */
    @Override
    public boolean hasNext()
    {
        return !(position > arrayEE.length - 1 || arrayEE[position] == null);
    }
    
    /**
     * 就是当前的这一个是否存在
     */
    @Override
    public Object next()
    {
        Employee ee = arrayEE[position];
        position++;
        return ee;
    }
    
}
