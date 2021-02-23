package com.yline.opengl.common

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.SurfaceHolder

class CommonGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private var callback: ((holder: SurfaceHolder?) -> Unit)? = null

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        super.surfaceDestroyed(holder)

        callback?.invoke(holder)
    }

    fun setOnSurfaceCallback(callback: ((holder: SurfaceHolder?) -> Unit)?) {
        this.callback = callback
    }
}