package com.yline.opengl.basic

import android.view.SurfaceHolder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 在什么地方进行绘制？
 *      坐标: [-1, 1]
 *
 * 绘制成什么形状？
 *      点
 *      线
 *      三角形
 *
 * 用什么颜色来绘制？
 *
 *
 */
interface OnShape {
    /**
     * 当 GLSurfaceView 创建时调用，主要做一些准备工作。
     */
    fun onSurfaceCreated(gl: GL10?, config: EGLConfig?)

    /**
     * 当 GLSurfaceView 视图改变时调用，第一次创建时也会被调用。
     */
    fun onSurfaceChanged(gl: GL10?, width: Int, height: Int)

    /**
     * 每一帧绘制时被调用。
     */
    fun onDrawFrame(gl: GL10?)

    /**
     * SurfaceView 结束时，回调
     */
    fun onSurfaceDestroyed(holder: SurfaceHolder?)
}