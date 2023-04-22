package com.fossil.technical.gallery.extention

import android.net.Uri
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fossil.technical.gallery.R

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
fun ImageView.loadImageByURI(uri : Uri){
    Glide.with(this)
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_loading)
        .into(this)

}
fun View.visible(){
    this.visibility = View.VISIBLE
}
fun View.gone(){
    this.visibility = View.GONE
}
fun View.invisible(){
    this.visibility = View.INVISIBLE
}