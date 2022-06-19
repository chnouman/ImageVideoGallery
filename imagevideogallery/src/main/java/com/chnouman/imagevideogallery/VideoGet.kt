package com.chnouman.imagevideogallery

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.chnouman.imagevideogallery.models.VideoContent
import com.chnouman.imagevideogallery.models.VideoFolderContent

class VideoGet private constructor(context: Context) {
    private val videoContext: Context

    @SuppressLint("InlinedApi")
    var Projections = arrayOf(
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.ALBUM,
        MediaStore.Video.Media.DATE_TAKEN,
        MediaStore.Video.Media.ARTIST
    )

    /**Returns an Arraylist of [VideoContent]   */
    @SuppressLint("InlinedApi")
    fun getAllVideoContent(contentLocation: Uri?): ArrayList<VideoContent> {
        val allVideo = ArrayList<VideoContent>()
        cursor = videoContext.contentResolver.query(
            contentLocation!!,
            Projections,
            null,
            null,
            "LOWER (" + MediaStore.Video.Media.DATE_TAKEN + ") DESC"
        ) //DESC ASC
        try {
            cursor!!.moveToFirst()
            do {
                val videoContent = VideoContent()
                videoContent.videoName = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Video.Media.DISPLAY_NAME
                    )
                )
                videoContent.path =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                videoContent.videoDuration = cursor!!.getLong(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Video.Media.DURATION
                    )
                )
                videoContent.videoSize =
                    cursor!!.getLong(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                val id = cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                videoContent.videoId = id.toLong()
                val contentUri = Uri.withAppendedPath(contentLocation, id.toString())
                videoContent.videoUri = contentUri.toString()
                videoContent.album =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM))
                videoContent.artist =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST))
                allVideo.add(videoContent)
            } while (cursor!!.moveToNext())
            cursor!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return allVideo
    }

    /**Returns an Arraylist of [VideoContent] in a specific folder   */
    @SuppressLint("InlinedApi")
    fun getAllVideoContentByBucketId(bucketId: Int): ArrayList<VideoContent> {
        val videoContents = ArrayList<VideoContent>()
        cursor = videoContext.contentResolver.query(
            externalContentUri,
            Projections,
            MediaStore.Video.Media.BUCKET_ID + " like ? ",
            arrayOf("%$bucketId%"),
            "LOWER (" + MediaStore.Video.Media.DATE_TAKEN + ") DESC"
        ) //DESC
        try {
            cursor!!.moveToFirst()
            do {
                val videoContent = VideoContent()
                videoContent.videoName = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Video.Media.DISPLAY_NAME
                    )
                )
                videoContent.path =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                videoContent.videoDuration = cursor!!.getLong(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Video.Media.DURATION
                    )
                )
                videoContent.videoSize =
                    cursor!!.getLong(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                val id = cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                videoContent.videoId = id.toLong()
                val contentUri = Uri.withAppendedPath(externalContentUri, id.toString())
                videoContent.videoUri = contentUri.toString()
                videoContent.album =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM))
                videoContent.artist =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST))
                videoContents.add(videoContent)
            } while (cursor!!.moveToNext())
            cursor!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return videoContents
    }

    /**Returns an Arraylist of [videoFolderContent] with each videoFolderContent having an Arraylist of all it videoContent */
    @SuppressLint("InlinedApi")
    fun getAllVideoFolders(contentLocation: Uri?): ArrayList<VideoFolderContent> {
        val allVideoFolders = ArrayList<VideoFolderContent>()
        val videoPaths = ArrayList<Int>()
        cursor = videoContext.contentResolver.query(
            contentLocation!!, Projections,
            null, null, "LOWER (" + MediaStore.Video.Media.DATE_TAKEN + ") DESC"
        ) //DESC
        try {
            cursor!!.moveToFirst()
            do {
                val videoFolder = VideoFolderContent()
                val videoContent = VideoContent()
                videoContent.videoName = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Video.Media.DISPLAY_NAME
                    )
                )
                videoContent.path =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                videoContent.videoDuration = cursor!!.getLong(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Video.Media.DURATION
                    )
                )
                videoContent.videoSize =
                    cursor!!.getLong(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                val id = cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                videoContent.videoId = id.toLong()
                val contentUri = Uri.withAppendedPath(contentLocation, id.toString())
                videoContent.videoUri = contentUri.toString()
                videoContent.album =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM))
                videoContent.artist =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST))
                val folder =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                val datapath =
                    cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val bucket_id =
                    cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID))
                var folderpaths = datapath.substring(0, datapath.lastIndexOf("$folder/"))
                folderpaths = "$folderpaths$folder/"
                if (!videoPaths.contains(bucket_id)) {
                    videoPaths.add(bucket_id)
                    videoFolder.bucketId = bucket_id
                    videoFolder.folderPath = folderpaths
                    videoFolder.folderName = folder
                    videoFolder.videoFiles.add(videoContent)
                    allVideoFolders.add(videoFolder)
                } else {
                    for (i in allVideoFolders.indices) {
                        if (allVideoFolders[i].bucketId == bucket_id) {
                            allVideoFolders[i].videoFiles.add(videoContent)
                        }
                    }
                }
            } while (cursor!!.moveToNext())
            cursor!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return allVideoFolders
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var videoGet: VideoGet? = null
        val externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val internalContentUri = MediaStore.Video.Media.INTERNAL_CONTENT_URI
        private var cursor: Cursor? = null
        fun getInstance(contx: Context): VideoGet {
            if (videoGet == null) {
                videoGet = VideoGet(contx)
            }
            return videoGet!!
        }
    }

    init {
        videoContext = context.applicationContext
    }
}