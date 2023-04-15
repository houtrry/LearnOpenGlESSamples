package com.houtrry.learnopenglessamples.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.houtrry.learnopenglessamples.opengl.SampleRender

class MyGlSurfaceView(context: Context, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs) {

    private val simpleRender: SampleRender

    init {
        setEGLContextClientVersion(2)
        simpleRender = SampleRender(context)
        setRenderer(simpleRender)
    }
}