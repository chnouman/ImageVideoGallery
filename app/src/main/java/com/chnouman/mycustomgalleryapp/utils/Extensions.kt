package com.chnouman.mycustomgalleryapp.utils

import android.view.View

fun View.gone() {
    visibility = View.GONE
}
fun View.isGone(): Boolean {
    return visibility == View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}
