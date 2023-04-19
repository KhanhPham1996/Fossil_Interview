package com.fossil.technical.gallery.extention

import android.os.SystemClock
import android.view.View

fun View.singleClick(thresholdMs: Long = 300, listener: () -> Unit) {
    var lastClickTime: Long = 0
    setOnClickListener {
        val realTime = SystemClock.elapsedRealtime()
        if (realTime - lastClickTime > thresholdMs) {
            lastClickTime = realTime
            listener()
        }
    }
}