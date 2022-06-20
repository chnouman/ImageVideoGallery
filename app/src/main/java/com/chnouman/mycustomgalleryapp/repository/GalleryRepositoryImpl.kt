package com.chnouman.mycustomgalleryapp.repository

import android.net.Uri
import com.chnouman.imagevideogallery.PictureGet
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.imagevideogallery.models.PictureFolderContent
import com.chnouman.imagevideogallery.models.VideoFolderContent
import java.util.ArrayList

class GalleryRepositoryImpl(private val pictureGet: PictureGet, private val videoGet: VideoGet) :
    GalleryRepository {

    override fun getAllVideoFolders(externalContentUri: Uri?): ArrayList<VideoFolderContent> {
        return videoGet.getAllVideoFolders(externalContentUri)
    }

    override fun getAllPictureFolders(): ArrayList<PictureFolderContent> {
        return pictureGet.allPictureFolders
    }
}