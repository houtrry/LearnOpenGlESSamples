package com.houtrry.learnopenglessamples.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import android.util.Log
import com.houtrry.learnopenglessamples.R
import com.houtrry.learnopenglessamples.utils.readRawText
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SampleRender2(private val context: Context) : Renderer {

    companion object {
        private const val TAG = "SampleRender"
        private const val A_POSITION = "a_Position"
        private const val A_COLOR = "a_Color"
        private const val U_MATRIX = "u_Matrix"
        private const val BYTES_PRE_FLOAT = 4
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3
        private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PRE_FLOAT
    }

    private var vertexData: FloatBuffer
    private val tableVertices = floatArrayOf(
        //以GL_TRIANGLE_FAN的方式画三角形
        //也即是，第一个点（这里是(0, 0)点为中心点，依次与后面两个相邻的点组成三角形，画出三角形）
        //需要注意的是，最后一个点，要与第二个点一致，以保证最后一个三角形能画上
            0f,     0f,     1.0f, 1.0f, 1.0f,
        -0.52f, -0.81f,     0.7f, 0.7f, 1.0f,
        -0.52f,  0.81f,     0.7f, 1.0f, 0.7f,
         0.52f,  0.81f,     1.0f, 0.7f, 0.7f,
         0.52f, -0.81f,     0.7f, 1.0f, 0.7f,
        -0.52f, -0.81f,     0.7f, 0.7f, 1.0f,

           0f,      0f,     1.0f, 1.0f, 1.0f,
        -0.5f,   -0.8f,     0.7f, 0.7f, 0.7f,
        -0.5f,    0.8f,     0.7f, 0.7f, 0.7f,
         0.5f,    0.8f,     0.7f, 0.7f, 0.7f,
         0.5f,   -0.8f,     0.7f, 0.7f, 0.7f,
        -0.5f,   -0.8f,     0.7f, 0.7f, 0.7f,

        -0.5f,      0f,     0.0f, 0.0f, 1.0f,
         0.5f,      0f,     0.0f, 1.0f, 1.0f,

           0f,   0.25f,     1.0f, 0.0f, 0.0f,
           0f,  -0.25f,     0.0f, 1.0f, 0.0f,
    )

    private var aPositionLocation: Int = -1
    private var aColorLocation: Int = -1
    private var uMatrixLocation: Int = -1
    private val projectionMatrix = FloatArray(16) {
        0f
    }

    init {
        vertexData = ByteBuffer.allocateDirect(tableVertices.size * BYTES_PRE_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(tableVertices)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        initProgram()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val aspectRatio = if (width > height) width * 1.0f / height else height * 1.0f / width
        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0,
                -aspectRatio, aspectRatio,
                -1f, 1f,
                -1f, 1f
            )
        } else {
            Matrix.orthoM(projectionMatrix, 0,
                -1f, 1f,
                -aspectRatio, aspectRatio,
                -1f, 1f
            )
        }

        //平移
//        Matrix.translateM(projectionMatrix, 0, 0.25f, 0.25f, 0f)
    //缩放x，y
    //        Matrix.scaleM(projectionMatrix, 0, 1.5f, 1.5f, 1f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        //给矩阵赋值
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1,
            false, projectionMatrix, 0)
        //画桌面外边框
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
        //画桌面
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 6, 6)
        //画中间的线
        GLES20.glDrawArrays(GLES20.GL_LINES, 12, 2)
        //画两个球
        GLES20.glDrawArrays(GLES20.GL_POINTS, 14, 1)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 15, 1)
    }

    private fun initProgram() {
        val simpleVertexShader = context.readRawText(R.raw.simple_vertex_shader2)
        val simpleFragmentShader = context.readRawText(R.raw.simple_fragment_shader2)
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
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR)
        uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX)

        //关联属性和顶点数据的数组
        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )

        //使能顶点数据
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        //数据移动到color数据开始的地方
        //每一个数据有两个float表示坐标，3个float表示颜色
        //因此，此处，对于a_Color，
        //1. 把数据移动到color数据开始的地方，也就是跳过前两个float
        //2. 声明数据怎么用
        //      a. 每个color由3个float组成
        //关联a_Color
        vertexData.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT, //每个color使用3个float
            GLES20.GL_FLOAT,//数据由float组成
            false,
            STRIDE,//color与color数据之间的间隔是5个float，每个float4个字节，因此，此处是5*4=20，尤其要注意的是，如果数据是紧密排列的，比如，color后面还是color，没有其他数据，那么，此时跨距可以直接写0，0是一个特殊值，表示数据是紧密排列的
            vertexData)
        GLES20.glEnableVertexAttribArray(aColorLocation)


    }
}