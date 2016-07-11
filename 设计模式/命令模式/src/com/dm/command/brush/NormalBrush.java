package com.dm.command.brush;

import android.graphics.Path;

public class NormalBrush implements IBrush
{
    
    @Override
    public void down(Path path, float x, float y)
    {
        path.moveTo(x, y);
    }
    
    @Override
    public void move(Path path, float x, float y)
    {
        path.lineTo(x, y);
    }
    
    @Override
    public void up(Path path, float x, float y)
    {
        
    }
    
}
