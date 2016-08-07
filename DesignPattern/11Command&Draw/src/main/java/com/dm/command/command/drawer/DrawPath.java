package com.dm.command.command.drawer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DrawPath implements IDraw
{
    private Path path; // 需要绘制的路径
    
    private Paint paint; // 绘制画笔
    
    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawPath(path, paint);
    }
    
    @Override
    public void undo()
    {
        
    }
    
    public void setPath(Path path)
    {
        this.path = path;
    }
    
    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }
    
    public Path getPath()
    {
        return this.path;
    }
}
