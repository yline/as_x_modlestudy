package com.yline.opengl.basic.rectangle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.yline.base.BaseActivity
import com.yline.opengl.basic.OnShape
import com.yline.opengl.common.CommonGLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * # onSurfaceCreated
 * 1-1, buildProgram, 构建使用的 OpenGL 程序, 返回 id
 * 1-2, glUseProgram, 确定使用 OpenGL 程序, 传入 id
 *
 * # onSurfaceCreated
 * 2-1, glGetUniformLocation、glGetAttribLocation 绑定 java内存 和 C内存
 * 2-2, glVertexAttribPointer、glEnableVertexAttribArray 来绑定 结果值 和 开启使用
 *
 * # onDrawFrame
 * 3-1, glUniform4f、glUniformMatrix4fv 进行赋值
 * 3-2, glDrawArrays 进行最后的 执行
 */
class BasicRectangleActivity() : BaseActivity() {
    companion object {
        fun launch(context: Context?) {
            if (null != context) {
                val intent = Intent()
                intent.setClass(context, BasicRectangleActivity::class.java)
                if (context !is Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }

    private lateinit var mGLSurfaceView: CommonGLSurfaceView

    private var mShape: OnShape? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 创建 View
        mGLSurfaceView = CommonGLSurfaceView(this)
        mGLSurfaceView.setEGLContextClientVersion(2)    // 代表，选择 OpenGL ES 2.0 版本
        mGLSurfaceView.setRenderer(object : GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                // 初始化
                mShape = SampleRectangle(this@BasicRectangleActivity)    // 这里是 矩形
                mShape?.onSurfaceCreated(gl, config)
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                mShape?.onSurfaceChanged(gl, width, height)
            }

            override fun onDrawFrame(gl: GL10?) {
                mShape?.onDrawFrame(gl)
            }
        })
        mGLSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        mGLSurfaceView.setOnSurfaceCallback {
            mShape?.onSurfaceDestroyed(it)
        }

        setContentView(mGLSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLSurfaceView.onPause()
    }
}