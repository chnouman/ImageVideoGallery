package com.chnouman.mycustomgalleryapp.repository

import android.net.Uri
import com.chnouman.imagevideogallery.models.PictureFolderContent
import com.chnouman.imagevideogallery.models.VideoFolderContent
import java.util.ArrayList

interface GalleryRepository {
    //TODO suspend: run them on coroutine
    fun getAllVideoFolders(externalContentUri: Uri?): ArrayList<VideoFolderContent>
    fun getAllPictureFolders(): ArrayList<PictureFolderContent>
}