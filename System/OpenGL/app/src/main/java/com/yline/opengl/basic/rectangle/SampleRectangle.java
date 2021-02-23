package com.yline.opengl.basic.rectangle;

import android.content.Context;
import android.opengl.GLES20;
import android.view.SurfaceHolder;

import com.yline.opengl.R;
import com.yline.opengl.basic.OnShape;
import com.yline.opengl.common.ShaderHelper;
import com.yline.opengl.common.VertexArray;

import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

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
    protected Context mContext;

    protected VertexArray vertexArray;

    protected VertexArray indexArray;

    protected int mProgram;

    protected float[] modelMatrix = new float[16];
    protected float[] viewMatrix = new float[16];
    protected float[] projectionMatrix = new float[16];
    protected float[] mvpMatrix = new float[16];

    protected int POSITION_COMPONENT_COUNT;

    protected int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

    protected int STRIDE;
    //////////////////////////

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    public static final String U_PRO_MATRIX = "u_ProMatrix";
    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    private static final String U_TEXTURE_UNIT = "u_TextureUnit";

    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    private int uProMatrixLocation;
    private int aTextureCoordinatesLocation;
    private int uTextureUnitLocation;

    // 包含顶点坐标和纹理坐标
    float[] rectangleVertex = {
            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f
    };

    float[] reactangleElementVertex = {
            0f, 0f,
            -0.5f, -0.8f,
            0.5f, -0.8f,
            0.5f, 0.8f,
            -0.5f, 0.8f,
            -0.5f, -0.8f
    };

    private ShortBuffer indexBuffer;

    private short[] indices = {
            0, 1, 2,
            0, 2, 3,
            0, 3, 4,
            0, 4, 1
    };

    float[] textureElementVertex = {
            0.5f, 0.5f,
            0f, 0.9f,
            1f, 0.9f,
            1f, 0.1f,
            0f, 0.1f
    };

    VertexArray textureVertexArray;

    public SampleRectangle(Context context) {
        // 不适用纹理的着色器脚本
        mProgram = ShaderHelper.buildProgram(context, R.raw.rectangle_vertex_shaper, R.raw.rectangle_fragment_shaper);
        // 使用纹理的着色器脚本
//        mProgram = ShaderHelper.buildProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        glUseProgram(mProgram);

        initRectangleVertex();

        vertexArray = new VertexArray(reactangleElementVertex);

        textureVertexArray = new VertexArray(textureElementVertex);

        POSITION_COMPONENT_COUNT = 2;

        TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

//        STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PRE_FLOAT;

        STRIDE = 0;

        indexBuffer = ByteBuffer.allocateDirect(indices.length * 2).order(ByteOrder.nativeOrder())
                .asShortBuffer().put(indices);

        indexBuffer.position(0);
    }

    /**
     * 采用 glDrawElement 方式来定义顶点数组、索引位置、还有纹理位置
     */
    private void initRectangleVertex() {

    }

    @Override
    public void onSurfaceCreated(@Nullable GL10 gl, @Nullable EGLConfig config) {
// 不使用纹理的单一颜色
        aColorLocation = glGetUniformLocation(mProgram, U_COLOR);

        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);

        // 对应纹理的设置
//        aTextureCoordinatesLocation = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);
//        uTextureUnitLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        vertexArray.setVertexAttribPointer(0, aPositionLocation, POSITION_COMPONENT_COUNT, STRIDE);

        // 使能 纹理的数据
//        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, aTextureCoordinatesLocation,
//                TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);

//        textureVertexArray.setVertexAttribPointer(0, aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);

        setIdentityM(mvpMatrix, 0);

        setIdentityM(projectionMatrix, 0);

//        int texture = TextureHelper.loadTexture(mContext, R.drawable.texture);

        // OpenGL 在使用纹理进行绘制时，不需要直接给着色器传递纹理。
        // 相反，我们使用纹理单元保存那个纹理，因为，一个 GPU 只能同时绘制数量有限的纹理
        // 它使用那些纹理单元表示当前正在被绘制的活动的纹理

        // 通过调用 glActiveTexture 把活动的纹理单元设置为纹理单元 0
//        glActiveTexture(GL_TEXTURE0);
        // 然后通过调用 glBindTexture 把纹理绑定到这个单元
//        glBindTexture(GL_TEXTURE_2D, texture);
        // 接着通过调用 glUniform1i 把被选定的纹理单元传递给片段着色器中的 u_TextureUnit
//        glUniform1i(uTextureUnitLocation, 0);
    }

    @Override
    public void onSurfaceChanged(@Nullable GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(@Nullable GL10 gl) {
        // 绘制 之前 清除颜色 和 清屏
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        //
        glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);

        glUniformMatrix4fv(uProMatrixLocation, 1, false, projectionMatrix, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

//        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_SHORT, indexBuffer);
    }

    @Override
    public void onSurfaceDestroyed(@Nullable SurfaceHolder holder) {

    }
}
