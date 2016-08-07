package com.dm.command.brush;

import android.graphics.Path;

public interface IBrush
{
    /**
     * 触点接触时
     * @param path	路劲对象
     * @param x		当前x坐标
     * @param y		当前y坐标
     */
    void down(Path path, float x, float y);
    
    /**
     * 触点移动时
     * @param path	路劲对象
     * @param x		当前x坐标
     * @param y		当前y坐标
     */
    void move(Path path, float x, float y);
    
    /**
     * 触点离开时
     * @param path	路劲对象
     * @param x		当前x坐标
     * @param y		当前y坐标
     */
    void up(Path path, float x, float y);
}
