package com.chnouman.mycustomgalleryapp.backend.repository

import android.net.Uri
import com.chnouman.imagevideogallery.PictureGet
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.imagevideogallery.models.PictureFolderContent
import com.chnouman.imagevideogallery.models.VideoFolderContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayList

class GalleryRepositoryImpl(private val pictureGet: PictureGet, private val videoGet: VideoGet) :
    GalleryRepository {

    override suspend fun getAllVideoFolders(externalContentUri: Uri?): ArrayList<VideoFolderContent> {
        return withContext(Dispatchers.IO) {videoGet.getAllVideoFolders(externalContentUri)}
    }

    override suspend fun getAllPictureFolders(): ArrayList<PictureFolderContent> {
        return withContext(Dispatchers.IO) {pictureGet.allPictureFolders}
    }

    override suspend fun getAllPictureContents(externalContentUri: Uri?): Collection<Any> {
        return withContext(Dispatchers.IO) { pictureGet.getAllPictureContents(externalContentUri) }
    }

    override suspend fun getAllVideoContent(externalContentUri: Uri?): Collection<Any> {
        return withContext(Dispatchers.IO) { videoGet.getAllVideoContent(VideoGet.externalContentUri) }
    }

    override suspend fun getAllPictureContentByBucketId(bucketId: Int): Collection<Any> {
        return withContext(Dispatchers.IO) { pictureGet.getAllPictureContentByBucketId(bucketId) }
    }

    override suspend fun getAllVideoContentByBucketId(bucketId: Int): Collection<Any> {
        return withContext(Dispatchers.IO) { videoGet.getAllVideoContentByBucketId(bucketId) }
    }
}