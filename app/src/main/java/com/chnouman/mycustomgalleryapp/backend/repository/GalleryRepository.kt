package com.chnouman.mycustomgalleryapp.backend.repository

import android.net.Uri
import com.chnouman.imagevideogallery.models.PictureFolderContent
import com.chnouman.imagevideogallery.models.VideoFolderContent
import java.util.ArrayList

interface GalleryRepository {
    suspend fun getAllVideoFolders(externalContentUri: Uri?): ArrayList<VideoFolderContent>
    suspend fun getAllPictureFolders(): ArrayList<PictureFolderContent>
    suspend fun getAllPictureContents(externalContentUri: Uri?): Collection<Any>
    suspend fun getAllVideoContent(externalContentUri: Uri?): Collection<Any>
    suspend fun getAllPictureContentByBucketId(bucketId: Int): Collection<Any>
    suspend fun getAllVideoContentByBucketId(bucketId: Int): Collection<Any>
}