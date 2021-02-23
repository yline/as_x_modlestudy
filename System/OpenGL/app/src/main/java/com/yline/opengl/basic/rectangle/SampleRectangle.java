package com.yline.opengl.basic.rectangle;

import android.content.Context;
import android.opengl.GLES20;
import android.view.SurfaceHolder;

import com.yline.opengl.R;
import com.yline.opengl.basic.OnShape;
import com.yline.opengl.common.VertexArray;
import com.yline.opengl.utils.ShaderUtil;

import org.jetbrains.annotations.Nullable;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.setIdentityM;

public class SampleRectangle implements OnShape {
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private static final float[] rectangleElementVertex = {
            0f, 0f,
            -0.5f, -0.8f,
            0.5f, -0.8f,
            0.5f, 0.8f,
            -0.5f, 0.8f,
            -0.5f, -0.8f
    };

    private final VertexArray mVertexArray;
    private final int mProgram;

    private final float[] projectionMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];

    private final int POSITION_COMPONENT_COUNT;
    private final int STRIDE;

    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    public SampleRectangle(Context context) {
        // 不适用纹理的着色器脚本, 构建 程序
        mProgram = ShaderUtil.INSTANCE.buildProgram(context, R.raw.rectangle_vertex_shaper, R.raw.rectangle_fragment_shaper);
        // 使用程序
        glUseProgram(mProgram);

        mVertexArray = new VertexArray(rectangleElementVertex);

        POSITION_COMPONENT_COUNT = 2;
        STRIDE = 0;
    }

    @Override
    public void onSurfaceCreated(@Nullable GL10 gl, @Nullable EGLConfig config) {
        // 不使用纹理的单一颜色
        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        // 给绑定的值赋值，也就是从顶点数据那里开始读取，每次读取间隔是多少
        mVertexArray.setVertexAttrPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, STRIDE);

        setIdentityM(mvpMatrix, 0);

        setIdentityM(projectionMatrix, 0);
    }

    @Override
    public void onSurfaceChanged(@Nullable GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(@Nullable GL10 gl) {
        // 绘制 之前 清除颜色 和 清屏
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // 绑定值
        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        // 绑定值
        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);

        // 给绑定的值赋值
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }

    @Override
    public void onSurfaceDestroyed(@Nullable SurfaceHolder holder) {

    }
}
