package com.dm.builder.director;

import com.dm.builder.builder.Builder;

/**
 * 负责构建Computer
 */
public class MacbookDirector
{
    Builder mBuilder = null;
    
    public MacbookDirector(Builder builder)
    {
        this.mBuilder = builder;
    }
    
    /** 创建对象 */
    public void construct(String board, String display)
    {
        mBuilder.buildBoard(board);
        mBuilder.buildDisplay(display);
        mBuilder.buildOS();
    }
    
}
