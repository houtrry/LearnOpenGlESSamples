package com.houtrry.learnopenglessamples.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import com.houtrry.learnopenglessamples.R
import com.houtrry.learnopenglessamples.utils.readRawText
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SampleRender(private val context: Context) : Renderer {

    companion object {
        private const val TAG = "SampleRender"
        private const val U_COLOR = "u_Color"
        private const val A_POSITION = "a_Position"
        private const val BYTES_PRE_FLOAT = 4
        private const val POSITION_COMPONENT_COUNT = 2
    }

    private lateinit var vertexData: FloatBuffer
    private val tableVertices = floatArrayOf(
        0f, 0f,
        9f, 14f,
        0f, 14f,

        0f, 0f,
        9f, 0f,
        9f, 14f,

        0f, 7f,
        9f, 7f,

        4.5f, 2f,
        4.5f, 12f,
    )

    private var uColorLocation: Int = -1
    private var aPositionLocation: Int = -1

    init {
        vertexData = ByteBuffer.allocateDirect(tableVertices.size * BYTES_PRE_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1.0f, 0f, 0f, 0f)
        initProgram()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

        GLES20.glUniform4f(uColorLocation, 1f, 0f, 0f, 1f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)


        GLES20.glUniform4f(uColorLocation, 0f, 0f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)

        GLES20.glUniform4f(uColorLocation, 1f, 0f, 0f, 1f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }

    private fun initProgram() {
        val simpleVertexShader = context.readRawText(R.raw.simple_vertex_shader)
        val simpleFragmentShader = context.readRawText(R.raw.simple_fragment_shader)
        Log.d(TAG, "simpleVertexShader: $simpleVertexShader")
        Log.d(TAG, "simpleFragmentShader: $simpleFragmentShader")
        if (simpleVertexShader.isEmpty() || simpleFragmentShader.isEmpty()) {
            Log.e(
                TAG,
                "vertexShader($simpleVertexShader) or fragmentShader($simpleFragmentShader) is null"
            )
            return
        }
        val vertexShaderId = ShaderHelper.compileVertexShader(simpleVertexShader)
        if (vertexShaderId == 0) {
            Log.e(TAG, "create vertex shader failure")
            return
        }
        val fragmentShaderId = ShaderHelper.compileFragmentShader(simpleFragmentShader)
        if (fragmentShaderId == 0) {
            Log.e(TAG, "create fragment shader failure")
            return
        }
        val programId = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId)
        if (programId == 0) {
            Log.e(TAG, "link program failure")
            return
        }
        val validateProgram = ShaderHelper.isValidateProgram(programId)
        if (!validateProgram) {
            Log.e(TAG, "validate program failure")
            return
        }
        GLES20.glUseProgram(programId)

        //获取属性位置
        uColorLocation = GLES20.glGetUniformLocation(programId, U_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)

        //关联属性和顶点数据的数组
        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            0,
            vertexData
        )

        //使能顶点数据
        GLES20.glEnableVertexAttribArray(aPositionLocation)

    }
}