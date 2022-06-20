package com.chnouman.imagevideogallery.models

import androidx.annotation.StringRes


class VideoFolderContent(folderPath: String? = null, folderName: String? = null) :
    FolderContent(folderPath, folderName) {
    var videoFiles = mutableListOf<VideoContent>()

    constructor(folderNameStringId: Int) : this("", "")
}