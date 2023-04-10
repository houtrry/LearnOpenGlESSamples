package com.houtrry.learnopenglessamples.utils

import android.app.ActivityManager
import android.content.Context

object GlUtils {
    /**
     * 检查系统是否支持OpenGL ES 2.0
     */
    fun isSupportES2(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        return (activityManager?.deviceConfigurationInfo?.reqGlEsVersion ?: 0) >= 0x20000
    }

}