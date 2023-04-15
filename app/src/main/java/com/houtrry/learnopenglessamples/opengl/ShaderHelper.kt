package com.houtrry.learnopenglessamples.opengl

import android.opengl.GLES20
import android.util.Log
import javax.microedition.khronos.opengles.GL

object ShaderHelper {

    private const val TAG = "ShaderHelper"

    /**
     * 编译顶点着色器
     */
    fun compileVertexShader(shaderSourceText: String): Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderSourceText)
    }

    /**
     * 编译片源着色器
     */
    fun compileFragmentShader(shaderSourceText: String): Int {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderSourceText)
    }

    /**
     * 生成Program
     * 链接着色器
     */
    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        //1. 创建Program（创建一个在GPU上运行的程序）
        val programId = GLES20.glCreateProgram()
        if (programId == 0) {
            Log.e(TAG, "couldn't create program")
            return 0
        }
        //2. 关联顶点着色器和片源着色器
        GLES20.glAttachShader(programId, vertexShaderId)
        GLES20.glAttachShader(programId, fragmentShaderId)
        //3. 链接，使着色器生效
        GLES20.glLinkProgram(programId)
        //4. 检查链接状态
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
        Log.i(TAG, "link program info: ${GLES20.glGetShaderInfoLog(programId)}")
        if (linkStatus[0] == 0) {
            Log.e(TAG, "linking of program failure")
            GLES20.glDeleteProgram(programId)
            return 0
        }
        return programId
    }

    /**
     * 验证opengl程序对象
     */
    fun isValidateProgram(programId: Int): Boolean {
        GLES20.glValidateProgram(programId)
        val validateStatus = IntArray(1)
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
        Log.d(
            TAG,
            "result of validate program is ${validateStatus[0]}, and info is ${
                GLES20.glGetShaderInfoLog(programId)
            }"
        )
        return validateStatus[0] != 0
    }

    /**
     * 生成和编译Shader
     */
    private fun compileShader(type: Int, shaderSourceText: String): Int {
        //1. 创建新的着色器对象
        val shaderId = GLES20.glCreateShader(type)
        //如果shaderId，表示创建失败
        if (shaderId == 0) {
            Log.e(TAG, "couldn't create new shader")
            return 0
        }
        //2. 关联shaderId和着色器源码
        GLES20.glShaderSource(shaderId, shaderSourceText)
        //3. 编译着色器
        GLES20.glCompileShader(shaderId)
        //4. 检查编译状态
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        //通过glGetShaderInfoLog方法获取编译的信息(如果编译失败，此处会打印编译失败的信息)
        Log.e(TAG, "compile of shader info is ${GLES20.glGetShaderInfoLog(shaderId)}")
        //状态为0表示编译失败
        if (compileStatus[0] == 0) {
            //删除shaderId
            GLES20.glDeleteShader(shaderId)
            return 0
        }
        return shaderId
    }
}