package com.dm.memorandum.uml.caretaker;

import com.dm.memorandum.uml.memoto.Memoto;

/**
 * 负责管理 Memoto
 * @author f21
 * @date 2016-2-28
 */
public class Caretaker
{
    private Memoto mMemoto;
    
    /** 存档 */
    public void archive(Memoto memoto)
    {
        this.mMemoto = memoto;
    }
    
    public Memoto getMemoto()
    {
        return this.mMemoto;
    }
}
