package com.chnouman.imagevideogallery

import android.content.Context

class ImageVideoGallery {
    companion object {
        /**Returns a static instance of [VideoGet]  */
        fun withVideoContext(contx: Context): VideoGet {
            return VideoGet.getInstance(contx)
        }

        /**Returns a static instance of [PictureGet]  */
        fun withPictureContext(contx: Context): PictureGet {
            return PictureGet.getInstance(contx)
        }
    }
}