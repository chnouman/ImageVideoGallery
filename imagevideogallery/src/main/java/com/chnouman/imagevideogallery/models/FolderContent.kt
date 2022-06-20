package com.chnouman.imagevideogallery.models

import androidx.annotation.StringRes


open class FolderContent(var folderPath: String?, var folderName: String?) {
    @StringRes
    var folderNameStringId: Int? = null
    var bucketId = 0
    constructor(folderNameStringId:Int) : this("",""){
        this.folderNameStringId = folderNameStringId
    }
}