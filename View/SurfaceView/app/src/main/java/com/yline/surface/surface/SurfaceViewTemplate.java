package com.yline.surface.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yline.utils.LogUtil;

public class SurfaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder; // SurfaceHolder
    private Canvas mCanvas; // 用于绘图的Canvas
    private boolean mIsDrawing; // 子线程标志位
    private Paint mPaint; // 画笔
    private Path mPath; // 路径

    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mHolder = getHolder();
        //添加回调
        mHolder.addCallback(this);

        mPath = new Path();
        //初始化画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        startThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                while (mIsDrawing) {
                    draw();
                    long end = System.currentTimeMillis();
                    if (end - start < 100) {
                        try {
                            Thread.sleep(100 - end + start);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void draw() {
        try {
            //锁定画布并返回画布对象
            mCanvas = mHolder.lockCanvas();
            //接下去就是在画布上进行一下draw
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //当画布内容不为空时，才post，避免出现黑屏的情况。
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    /**
     * 绘制触摸滑动路径
     *
     * @param event MotionEvent
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.v("onTouchEvent: down");
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.v("onTouchEvent: move");
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.v("onTouchEvent: up");
                break;
        }
        return true;
    }

    /**
     * 清屏
     *
     * @return true
     */
    public boolean reset() {
        mPath.reset();
        return true;
    }
}
