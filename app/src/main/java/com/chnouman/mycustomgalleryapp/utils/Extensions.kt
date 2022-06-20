package com.chnouman.mycustomgalleryapp.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun View.gone() {
    visibility = View.GONE
}
fun View.isGone(): Boolean {
    return visibility == View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun ImageView.picture(uri: String){
    Glide.with(this.context)
        .load(Uri.parse(uri))
        .apply(RequestOptions().centerCrop())
        .into(this)
}
fun ImageView.pictureFit(uri: String){
    Glide.with(this.context)
        .load(Uri.parse(uri))
        .apply(RequestOptions().fitCenter())
        .into(this)
}