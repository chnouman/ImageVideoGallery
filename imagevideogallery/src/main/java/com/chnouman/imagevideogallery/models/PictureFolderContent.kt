package com.chnouman.imagevideogallery.models

class PictureFolderContent(path: String? = null, folderName: String? = null) :
    FolderContent(path, folderName) {
    var photos = mutableListOf<PictureContent>()
}