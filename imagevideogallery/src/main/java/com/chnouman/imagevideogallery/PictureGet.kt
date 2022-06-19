package com.chnouman.imagevideogallery

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.chnouman.imagevideogallery.models.PictureContent
import com.chnouman.imagevideogallery.models.PictureFolderContent

class PictureGet private constructor(context: Context) {
    private val pictureContext: Context

    @SuppressLint("InlinedApi")
    private val projections = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_TAKEN
    )

    /**Returns an ArrayList of [PictureContent]   */
    @SuppressLint("InlinedApi")
    fun getAllPictureContents(contentLocation: Uri?): ArrayList<PictureContent> {
        val images = ArrayList<PictureContent>()
        cursor = pictureContext.contentResolver.query(
            contentLocation!!, projections, null, null,
            "LOWER (" + MediaStore.Images.Media.DATE_TAKEN + ") DESC"
        )
        try {
            cursor!!.moveToFirst()
            do {
                val pictureContent = PictureContent()
                pictureContent.pictureName = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DISPLAY_NAME
                    )
                )
                pictureContent.picturePath = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DATA
                    )
                )
                pictureContent.pictureSize = cursor!!.getLong(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Images.Media.SIZE
                    )
                )
                val id =
                    cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                pictureContent.pictureId = id
                val contentUri = Uri.withAppendedPath(contentLocation, id.toString())
                pictureContent.photoUri = contentUri.toString()
                images.add(pictureContent)
            } while (cursor!!.moveToNext())
            cursor!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }

    /**Returns an ArrayList of [PictureContent] in a specific folder */
    @SuppressLint("InlinedApi")
    fun getAllPictureContentByBucketId(bucketId: Int): ArrayList<PictureContent> {
        val images = ArrayList<PictureContent>()
        cursor = pictureContext.contentResolver.query(
            externalContentUri,
            projections,
            MediaStore.Images.Media.BUCKET_ID + " like ? ",
            arrayOf(
                "%$bucketId%"
            ),
            "LOWER (" + MediaStore.Images.Media.DATE_TAKEN + ") DESC"
        )
        try {
            cursor!!.moveToFirst()
            do {
                val pictureContent = PictureContent()
                pictureContent.pictureName = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DISPLAY_NAME
                    )
                )
                pictureContent.picturePath = cursor!!.getString(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Images.Media.DATA
                    )
                )
                pictureContent.pictureSize = cursor!!.getLong(
                    cursor!!.getColumnIndexOrThrow(
                        MediaStore.Images.Media.SIZE
                    )
                )
                val id =
                    cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                pictureContent.pictureId = id
                val contentUri = Uri.withAppendedPath(externalContentUri, id.toString())
                pictureContent.photoUri = contentUri.toString()
                images.add(pictureContent)
            } while (cursor!!.moveToNext())
            cursor!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }

    /**Returns an ArrayList of [PictureFolderContent]   */ /* suspend fun getImages(count: Int, start: Int): Three<MutableList<Three<Uri?, String?, Date>>, Boolean, Int> {
        val imagesList = mutableListOf<Three<Uri?, String?, Date>>()
        var index = start

        return withContext(Dispatchers.IO) {
            while (imageCursor?.moveToPosition(i) == true) {
                val id = imageIdColumn?.let { imageCursor.getLong(it) }
                val dateModified = Date(TimeUnit.SECONDS.toMillis(imageCursor.getLong(imageDateModifiedColumn ?: 0)))

                val displayName = imageDisplayNameColumn?.let { imageCursor.getString(it) }
                val contentUri = id?.let {
                    ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            it
                    )
                }
                index++
                contentUri?.let { imagesList.add(Three(contentUri, displayName, dateModified)) }
                if (index == count)
                    break
            }
            val areAllLoaded = index == imagesToLoad
            return@withContext Three(imagesList, areAllLoaded, index)
        }
    }*/
    @get:SuppressLint("InlinedApi")
    val allPictureFolders: ArrayList<PictureFolderContent>
        get() {
            val absolutePictureFolders = ArrayList<PictureFolderContent>()
            val picturePaths = ArrayList<Int>()
            cursor = pictureContext.contentResolver.query(
                externalContentUri, projections, null, null,
                "LOWER (" + MediaStore.Images.Media.DATE_TAKEN + ") DESC"
            )
            try {
                cursor!!.moveToFirst()
                do {
                    val photoFolder = PictureFolderContent()
                    val pictureContent = PictureContent()
                    pictureContent.pictureName = cursor!!.getString(
                        cursor!!.getColumnIndexOrThrow(
                            MediaStore.Images.Media.DISPLAY_NAME
                        )
                    )
                    pictureContent.picturePath = cursor!!.getString(
                        cursor!!.getColumnIndexOrThrow(
                            MediaStore.Images.Media.DATA
                        )
                    )
                    pictureContent.pictureSize = cursor!!.getLong(
                        cursor!!.getColumnIndexOrThrow(
                            MediaStore.Images.Media.SIZE
                        )
                    )
                    val id =
                        cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    pictureContent.pictureId = id
                    pictureContent.photoUri =
                        Uri.withAppendedPath(externalContentUri, id.toString()).toString()
                    val folder =
                        cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    val dataPath =
                        cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    val bucketId =
                        cursor!!.getInt(cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID))
                    var folderpaths = dataPath.substring(0, dataPath.lastIndexOf("$folder/"))
                    folderpaths = "$folderpaths$folder/"
                    if (!picturePaths.contains(bucketId)) {
                        picturePaths.add(bucketId)
                        photoFolder.bucketId = bucketId
                        photoFolder.folderPath = folderpaths
                        photoFolder.folderName = folder
                        photoFolder.photos.add(pictureContent)
                        absolutePictureFolders.add(photoFolder)
                    } else {
                        for (folderX in absolutePictureFolders) {
                            if (folderX.bucketId == bucketId) {
                                folderX.photos.add(pictureContent)
                            }
                        }
                    }
                } while (cursor!!.moveToNext())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return absolutePictureFolders
        }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var pictureGet: PictureGet? = null
        val externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val internalContentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI
        private var cursor: Cursor? = null
        fun getInstance(context: Context): PictureGet {
            if (pictureGet == null) {
                pictureGet = PictureGet(context)
            }
            return pictureGet!!
        }
    }

    init {
        pictureContext = context.applicationContext
    }
}