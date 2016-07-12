package com.dm.cursor.aggregate;

import com.dm.cursor.iterator.IIterator;

public interface ICompany
{
    /**
     * 返回一个迭代器对象
     * @return
     */
    IIterator iIterator();
}
