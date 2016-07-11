package com.dm.command.receiver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dm.command.command.drawer.DrawPath;
import com.dm.command.invoker.DrawInvoker;

public class DrawCanvas extends SurfaceView implements SurfaceHolder.Callback
{
    public boolean isDrawing, isRunning; // 标识是否可以绘制、绘制线程是否可以运行
        
    private Bitmap mBitmap; // 绘制到的位图对象
    
    private DrawInvoker mInvoker; // 绘制命令请求对象
    
    private DrawThread mThread; // 绘制线程
    
    public DrawCanvas(Context context)
    {
        this(context, null);
    }
    
    public DrawCanvas(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public DrawCanvas(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        
        mInvoker = new DrawInvoker();
        mThread = new DrawThread();
        
        getHolder().addCallback(this);
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mThread.start();
        isRunning = true;
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        isRunning = false;
        while (retry)
        {
            try
            {
                mThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 增减一条绘制路径
     */
    public void add(DrawPath path)
    {
        mInvoker.add(path);
    }
    
    /**
     * 重做上一步撤销的绘制
     */
    public void redo()
    {
        isDrawing = true;
        mInvoker.redo();
    }
    
    /**
     * 撤销上一步的绘制
     */
    public void undo()
    {
        isDrawing = true;
        mInvoker.undo();
    }
    
    /**
     * 是否可以撤销
     * @return
     */
    public boolean canUndo()
    {
        return mInvoker.canUndo();
    }
    
    /**
     * 是否可以重做
     * @return
     */
    public boolean canRedo()
    {
        return mInvoker.canRedo();
    }
    
    private class DrawThread extends Thread
    {
        
        @Override
        public void run()
        {
            super.run();
            Canvas canvas = null;
            while (isRunning)
            {
                if (isDrawing)
                {
                    try
                    {
                        canvas = getHolder().lockCanvas(null);
                        if (mBitmap == null)
                        {
                            mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                        }
                        Canvas c = new Canvas(mBitmap);
                        c.drawColor(0, PorterDuff.Mode.CLEAR);
                        
                        mInvoker.execute(c);
                        
                        canvas.drawBitmap(mBitmap, 0, 0, null);
                    }
                    finally
                    {
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                    isDrawing = false;
                }
            }
        }
    }
}
