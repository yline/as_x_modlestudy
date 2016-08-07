package com.dm.cursor.iterator;

/**
 * 两个函数:
 * 1,当前位置的值 是否存在
 * 2,存在则返回当前的值,并且将position指向下一个
 * 
 * @author f21
 * @date 2016-3-2
 */
public interface IIterator
{
    /**
     * 当前位置是否存在
     * @return
     */
    boolean hasNext();
    
    /**
     * 返回当前位置的元素,并且将位置移至下一位
     * @return
     */
    Object next();
}
