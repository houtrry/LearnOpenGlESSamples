package com.houtrry.learnopenglessamples

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.houtrry.learnopenglessamples.opengl.SampleRender
import com.houtrry.learnopenglessamples.utils.GlUtils
import com.houtrry.learnopenglessamples.utils.showShortToast
import com.houtrry.learnopenglessamples.view.MyGlSurfaceView
import kotlinx.android.synthetic.main.activity_opengl_sample.*

class OpenglSampleActivity : AppCompatActivity() {

    private var glSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_sample)
        if (!GlUtils.isSupportES2(this)) {
            showShortToast("not support OpenGL ES 2.0")
            return
        }
        glSurfaceView = MyGlSurfaceView(this)
        frameLayout.addView(
            glSurfaceView,
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView?.onPause()
    }

    override fun onDestroy() {
        frameLayout.removeAllViews()
        super.onDestroy()
    }
}