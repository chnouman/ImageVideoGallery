package com.chnouman.imagevideogallery.models

import androidx.annotation.StringRes

class FolderWithOneImage(
    var imageUri: String? = null,
    var folderName: String? = null,
    var total: Int = 0,
    var mediaType: MediaTypes = MediaTypes.PICTURE,
    @StringRes var folderNameStringId: Int? = -1,
    var bucketId: Int
)