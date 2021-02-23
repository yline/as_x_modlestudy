package com.yline.opengl.utils

import android.content.Context
import android.opengl.GLES20
import com.yline.log.LogUtil
import com.yline.opengl.R

/**
 * 着色器帮助类 {
 *      1, 编译着色器 「compileShader」
 *      2, 创建 OpenGL 程序和着色器链接「linkProgram」
 *      3, 验证 OpenGL 程序「validateProgram」
 *      4, 确定使用 OpenGL 程序「userSample」
 * }
 * @author yline 2021/2/23 -- 8:04 PM
 */
object ShaderUtil {
    private const val TAG = "ShaderUtil"

    /**
     * 这个只是作为一个案例，真实是 外部调用
     */
    fun userSample(context: Context) {
        val program = buildProgram(context, R.raw.rectangle_vertex_shaper, R.raw.rectangle_fragment_shaper)
        // 使用纹理的着色器脚本
        GLES20.glUseProgram(program)
    }

    fun buildProgram(context: Context, vertexShaderSource: Int, fragmentShaderSource: Int): Int {
        val vertexString = ResourceReadUtil.readTextFileFromResource(context, vertexShaderSource)
        val textureString = ResourceReadUtil.readTextFileFromResource(context, fragmentShaderSource)
        return buildProgram(vertexString, textureString)
    }

    fun buildProgram(vertexShaderSource: String, fragmentShaderSource: String): Int {
        // 编译着色器
        val vertexShaderId = compileVertexShader(vertexShaderSource)
        val fragmentShaderId = compileFragmentShader(fragmentShaderSource)

        // 创建 OpenGL 程序和着色器链接
        val program = linkProgram(vertexShaderId, fragmentShaderId)

        // 验证 OpenGL 程序
        validateProgram(program)

        return program
    }

    /* ------------------------------ 编译着色器 ---------------------------- */
    /**
     * 编译顶点着色器
     */
    private fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
    }

    /**
     * 编译片段着色器
     */
    private fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * 根据类型编译着色器
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        // 根据不同的类型创建着色器 ID
        val shaderObjectId = GLES20.glCreateShader(type)
        if (shaderObjectId == 0) {
            LogUtil.e("$TAG could not create new shader")
            return 0
        }

        // 将着色器 ID 和着色器程序内容连接
        GLES20.glShaderSource(shaderObjectId, shaderCode)
        // 编译着色器
        GLES20.glCompileShader(shaderObjectId)

        // 以下为验证编译结果是否失败
        val compileStatsu = IntArray(1)
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatsu, 0)
        if (compileStatsu[0] == 0) {
            // 失败则删除
            LogUtil.e("$TAG compilation of shader failed")
            GLES20.glDeleteShader(shaderObjectId)
            return 0
        }
        return shaderObjectId
    }

    /* ------------------------------ 创建 OpenGL 程序和着色器链接 ---------------------------- */
    /**
     * 创建 OpenGL 程序 和 着色器链接
     */
    private fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        // 创建 OpenGL 程序 ID
        val programObjectId = GLES20.glCreateProgram()
        if (programObjectId == 0) {
            LogUtil.e("$TAG could not create new program")
            return 0
        }
        // 链接上 顶点着色器
        GLES20.glAttachShader(programObjectId, vertexShaderId)
        // 链接上 片段着色器
        GLES20.glAttachShader(programObjectId, fragmentShaderId)

        // 链接着色器之后，链接 OpenGL 程序
        GLES20.glLinkProgram(programObjectId)

        // 验证链接结果是否失败
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0)
        LogUtil.v("$TAG result of linking program: ${GLES20.glGetProgramInfoLog(programObjectId)}")
        if (linkStatus[0] == 0) {
            // 失败则删除
            LogUtil.e("$TAG Linking of program failed")
            GLES20.glDeleteProgram(programObjectId)
            return 0
        }
        return programObjectId
    }

    /* ------------------------------ 验证 OpenGL 程序 ---------------------------- */
    /**
     * 验证 OpenGL 程序
     */
    private fun validateProgram(programObjectId: Int): Boolean {
        // 验证 OpenGL 是否可用
        GLES20.glValidateProgram(programObjectId)

        // 验证链接结果是否失败
        val validateStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
        LogUtil.v("$TAG Result of validating program: ${validateStatus[0]} Log:${GLES20.glGetProgramInfoLog(programObjectId)}")
        return validateStatus[0] != 0
    }
}