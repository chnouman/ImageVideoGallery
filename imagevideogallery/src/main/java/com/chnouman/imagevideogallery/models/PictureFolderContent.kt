package com.chnouman.imagevideogallery.models
import java.util.ArrayList

class PictureFolderContent {
    var photos: ArrayList<PictureContent>
    var folderName: String? = null
    var folderPath: String? = null
    var bucket_id = 0

    constructor() {
        photos = ArrayList()
    }

    constructor(path: String?, folderName: String?) {
        folderPath = path
        this.folderName = folderName
        photos = ArrayList()
    }
}