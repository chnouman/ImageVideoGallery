package com.chnouman.imagevideogallery.models

class VideoPictureFolderContent {
    var videoFiles: ArrayList<VideoContent>? = null
    var photos: ArrayList<PictureContent>? = null
        private set
    var folderName: String? = null
    var folderPath: String? = null
    var bucket_id = 0

    constructor() {
        videoFiles = ArrayList()
    }

    constructor(
        folderPath: String?, folderName: String?, currentVideoFiles: ArrayList<VideoContent?>?,
        pictureContents: ArrayList<PictureContent>?
    ) {
        var videoFiles = currentVideoFiles
        this.folderName = folderName
        this.folderPath = folderPath
        photos = pictureContents
    }
}