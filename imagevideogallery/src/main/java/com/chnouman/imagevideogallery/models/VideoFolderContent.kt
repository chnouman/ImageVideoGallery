package com.chnouman.imagevideogallery.models

import java.util.ArrayList

class VideoFolderContent {
    var videoFiles: ArrayList<VideoContent>
    var folderName: String? = null
    var folderPath: String? = null
    var bucket_id = 0

    constructor() {
        videoFiles = ArrayList()
    }

    constructor(folderPath: String?, folderName: String?) {
        this.folderName = folderName
        this.folderPath = folderPath
        videoFiles = ArrayList()
    }
}