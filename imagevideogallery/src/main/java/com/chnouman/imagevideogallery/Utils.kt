package com.chnouman.imagevideogallery

import com.chnouman.imagevideogallery.models.*

object Utils {

    fun getTotalImagesCount(allPictureFolders: MutableList<PictureFolderContent>): Int {
        var total = 0
        for (folder in allPictureFolders) {
            total += folder.photos.size
        }
        return total
    }

    fun getTotalVideosCount(allVideoFolderContent: MutableList<VideoFolderContent>): Int {
        var total = 0
        for (folder in allVideoFolderContent) {
            total += folder.videoFiles.size
        }
        return total
    }

    fun getMediaType(o: Any?) =
        when (o) {
            is VideoFolderContent -> {
                MediaTypes.VIDEO
            }
            is PictureFolderContent -> MediaTypes.PICTURE
            is VideoPictureFolderContent -> MediaTypes.MIX
            else -> if ((o as FolderContent).folderNameStringId == R.string.all_images) MediaTypes.PICTURE else MediaTypes.VIDEO
        }


    fun getBucketId(o: Any) =
        when (o) {
            is VideoFolderContent -> o.bucketId
            is PictureFolderContent -> o.bucketId
            is VideoPictureFolderContent -> o.bucketId
            else -> -1
        }


    fun getFolderName(o: Any) =
        when (o) {
            is VideoFolderContent -> o.folderName
            is PictureFolderContent -> o.folderName
            is VideoPictureFolderContent -> o.folderName
            else -> ""
        }

    fun getFolderNameStringId(o: Any) =
        when (o) {
            is FolderContent -> o.folderNameStringId
            else -> -1
        }

    fun getTotal(o: Any) =
        when (o) {
            is VideoFolderContent -> o.videoFiles.size
            is PictureFolderContent -> o.photos.size
            is VideoPictureFolderContent -> (o.videoFiles.size) + (o.photos.size)
            else -> 0
        }

    fun getPath(o: Any) =
        when (o) {
            is VideoFolderContent -> o.videoFiles[0].videoUri
            is PictureFolderContent -> o.photos[0].photoUri
            is VideoPictureFolderContent -> o.photos[0].photoUri ?: ""
            else -> ""
        }
}
