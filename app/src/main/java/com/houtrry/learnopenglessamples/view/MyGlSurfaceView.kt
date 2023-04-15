package com.houtrry.learnopenglessamples.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.houtrry.learnopenglessamples.opengl.SampleRender2

class MyGlSurfaceView(context: Context, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs) {

    private val simpleRender: SampleRender2

    init {
        setEGLContextClientVersion(2)
        simpleRender = SampleRender2(context)
        setRenderer(simpleRender)
    }
}