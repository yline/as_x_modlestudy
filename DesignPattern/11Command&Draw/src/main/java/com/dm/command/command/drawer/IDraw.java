package com.dm.command.command.drawer;

import android.graphics.Canvas;

public interface IDraw
{
    /**
     * 绘制命令
     * @param canvas	画布对象
     */
    void draw(Canvas canvas);
    
    /**
     * 撤销命令
     */
    void undo();
}
