package com.chnouman.mycustomgalleryapp.data.repository

import android.content.SharedPreferences
import android.net.Uri
import com.chnouman.imagevideogallery.PictureGet
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.imagevideogallery.models.PictureFolderContent
import com.chnouman.imagevideogallery.models.VideoFolderContent
import com.chnouman.mycustomgalleryapp.utils.Constants.VIEW_MODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GalleryRepositoryImpl(
    private val pictureGet: PictureGet,
    private val videoGet: VideoGet,
    private val prefs: SharedPreferences
) :
    GalleryRepository {

    override suspend fun getAllVideoFolders(externalContentUri: Uri?): ArrayList<VideoFolderContent> {
        return withContext(Dispatchers.IO) { videoGet.getAllVideoFolders(externalContentUri) }
    }

    override suspend fun getAllPictureFolders(): ArrayList<PictureFolderContent> {
        return withContext(Dispatchers.IO) { pictureGet.allPictureFolders }
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

    override suspend fun saveUserViewModePrefs(viewMode: Int) {
        withContext(Dispatchers.IO) {
            prefs.edit().putInt(VIEW_MODE, viewMode).apply()
        }
    }

    override suspend fun getUserViewModePrefs(): Int = withContext(Dispatchers.IO) {
        prefs.getInt(VIEW_MODE, -1)
    }
}