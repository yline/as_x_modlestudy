package com.dm.memorandum.note.caretaker;

import java.util.ArrayList;
import java.util.List;

import com.dm.memorandum.note.memoto.Memoto;

/**
 * 管理 Memoto 对象
 * @author f21
 * @date 2016-2-29
 */
public class Caretaker
{
    private static final int MAX = 30;
    
    private List<Memoto> memotoList = new ArrayList<Memoto>(MAX);
    
    private int index = 0;
    
    public Caretaker()
    {
        
    }
    
    /**
     * 保存备忘录
     * @param memoto
     */
    public void saveMemoto(Memoto memoto)
    {
        if (memotoList.size() > MAX)
        {
            memotoList.remove(0);
        }
        memotoList.add(memoto);
        index = memotoList.size() - 1;
    }
    
    /**
     * 获取上一个存档,相当于撤销功能
     * @return
     */
    public Memoto getPrevMemoto()
    {
        index = index > 0 ? --index : index;
        if (null != memotoList && memotoList.size() > 0)
        {
            return memotoList.get(index);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 获取下一个存档,相当于重做功能
     * @return
     */
    public Memoto getNextMemoto()
    {
        index = index < memotoList.size() - 1 ? ++index : index;
        return memotoList.get(index); // 这里也有点问题..但模式没错
    }
    
}
