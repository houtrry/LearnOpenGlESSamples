package com.houtrry.learnopenglessamples.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.houtrry.learnopenglessamples.opengl.SampleRender3

class MyGlSurfaceView(context: Context, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs) {

    private val simpleRender: SampleRender3

    init {
        setEGLContextClientVersion(2)
        simpleRender = SampleRender3(context)
        setRenderer(simpleRender)
    }
}