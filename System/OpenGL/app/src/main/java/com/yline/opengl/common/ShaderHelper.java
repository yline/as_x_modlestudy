package com.yline.opengl.common;

import android.content.Context;

import com.yline.log.LogUtil;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {
    // 编译顶点着色器
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    // 编译片段着色器
    public static int compleFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        LogUtil.INSTANCE.v("vertex is " + vertexShaderSource + " frag is " + fragmentShaderSource);
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compleFragmentShader(fragmentShaderSource);

        program = linkProgram(vertexShader, fragmentShader);

        validateProgram(program);

        return program;
    }

    public static int buildProgram(Context context, int vertexShaderSource, int fragmentShaderSource) {

        String vertexString = TextResourceReader.readTextFileFromResource(context, vertexShaderSource);
        String textureString = TextResourceReader.readTextFileFromResource(context, fragmentShaderSource);

        return buildProgram(vertexString, textureString);
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            LogUtil.INSTANCE.v("Could not create new program");
            return 0;
        }
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        LogUtil.INSTANCE.v("Result of linking program:\n" + glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            LogUtil.INSTANCE.v("Linking of program failed");
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);

        LogUtil.INSTANCE.v("Result of validating program: " + validateStatus[0] + "\nLog:" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;

    }

    // 根据类型编译着色器
    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            LogUtil.INSTANCE.v("could not create new shader");
            return 0;
        }
        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);
        final int[] compileStatsu = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatsu, 0);
//        Timber.d("Result of compiling source:" + "\n" + shaderCode + "\n:"
//                + glGetShaderInfoLog(shaderObjectId));

        if ((compileStatsu[0] == 0)) {
            glDeleteShader(shaderObjectId);
            LogUtil.INSTANCE.v("Compilation of shader failed");
            return 0;
        }
        return shaderObjectId;
    }
}
