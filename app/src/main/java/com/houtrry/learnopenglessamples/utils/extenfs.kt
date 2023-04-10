package com.houtrry.learnopenglessamples.utils

import android.content.Context
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showShortToast(strResId: Int) {
    Toast.makeText(this, strResId, Toast.LENGTH_SHORT).show()
}

fun Context.readRawText(rewResId: Int): String {
    val sb = StringBuilder()
    BufferedReader(InputStreamReader(resources.openRawResource(rewResId))).use {
        sb.append(it.readText())
        sb.append("\n")
    }
    return sb.toString()
}