package com.chnouman.mycustomgalleryapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.util.Util
import com.chnouman.imagevideogallery.Utils
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.imagevideogallery.models.*
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val galleryRepository: GalleryRepository) :
    ViewModel() {

    private val _foldersData = MutableLiveData<MutableList<FolderWithOneImage>>()
    val foldersData: LiveData<MutableList<FolderWithOneImage>>
        get() = _foldersData

    fun getFolders() {
        val allVideoFolders: MutableList<VideoFolderContent> =
            galleryRepository
                .getAllVideoFolders(VideoGet.externalContentUri)
        val allPictureFolders: MutableList<PictureFolderContent> =
            galleryRepository.getAllPictureFolders()

        //check if we have some content to show
        val allFolders = mutableListOf<Any>().apply {
            if (allPictureFolders.isNotEmpty()) {
                add(FolderContent(R.string.all_images))
            }
            if (allVideoFolders.isNotEmpty()) {
                add(FolderContent(R.string.all_videos))
            }
        }
        val folders = mutableListOf<FolderWithOneImage>()
        if (allVideoFolders.isNotEmpty() || allPictureFolders.isNotEmpty()) {
            //combine the folders
            val combinedFolderNames = mutableListOf<String>()
            for (videosFolderContent in allVideoFolders) {
                var videoPictureFolder = false
                lateinit var videoPictureFolderContent: VideoPictureFolderContent
                for (pictureFolder in allPictureFolders) {
                    if (videosFolderContent.folderName
                            .equals(pictureFolder.folderName)
                    ) {
                        //combine the data
                        videoPictureFolderContent = VideoPictureFolderContent(
                            videosFolderContent.folderPath, videosFolderContent.folderName,
                            videosFolderContent.videoFiles, pictureFolder.photos
                        )
                        combinedFolderNames.add(videosFolderContent.folderName ?: "")
                        videoPictureFolder = true
                        continue
                    }
                }
                if (videoPictureFolder) {
                    allFolders.add(videoPictureFolderContent)
                } else {
                    //not found
                    allFolders.add(videosFolderContent)
                }
            }
            //make sure all the picture folders also added
            for (PictureFolderContent in allPictureFolders) {
                var isFoundAsCombine = false
                for (folderName in combinedFolderNames) {
                    if (PictureFolderContent.folderName.equals(folderName)) {
                        isFoundAsCombine = true
                        continue
                    }
                }
                if (!isFoundAsCombine) {
                    allFolders.add(PictureFolderContent)
                }
            }
            var firstImage = ""
            var firstVideo = ""
            for (i in allFolders.indices) {
                var path: String? = ""
                if (i == 0 && allFolders[i] is PictureFolderContent) {
                    //set paths later
                } else if (i == 0 && allFolders[i] is VideoFolderContent) {
                    //set path later
                } else if (i == 0 && allFolders[i] is PictureFolderContent && allFolders[i + 1] is VideoFolderContent) {
                    //set path later
                } else {
                    path = Utils.getPath(allFolders[i])
                    if (allFolders[i] is PictureFolderContent && firstImage.isEmpty()) {
                        firstImage = path ?: ""
                    } else if (allFolders[i] is VideoFolderContent && firstVideo.isEmpty()) {
                        firstVideo = path ?: ""
                    }
                }
                val folderName = Utils.getFolderName(allFolders[i])
                //get count
                val totalCount = when (folderName) {
                    "" -> {
                        if (Utils.getFolderNameStringId(allFolders[i]) == R.string.all_images) {
                            Utils.getTotalImagesCount(allPictureFolders)
                        } else if (Utils.getFolderNameStringId(allFolders[i]) == R.string.all_videos) {
                            Utils.getTotalVideosCount(allVideoFolders)
                        } else {
                            0
                        }
                    }
                    else -> {
                        Utils.getTotal(allFolders[i])
                    }
                }

                folders.add(
                    FolderWithOneImage(
                        path ?: "",
                        folderName ?: "", totalCount,
                        Utils.getMediaType(allFolders[i]),
                        //check if folder name is null than set the folderNameStringId
                        if (folderName.isNullOrBlank()) Utils.getFolderNameStringId(allFolders[i]) else -1
                    )
                )
            }
            //set path for all images and all videos here
            //check if gallery is for all images
            if (allFolders[0] is FolderContent && (allFolders[0] as FolderContent).folderNameStringId == R.string.all_images) {
                folders[0].imageUri = firstImage
            }
            if (allFolders[0] is FolderContent && (allFolders[0] as FolderContent).folderNameStringId == R.string.all_videos) {
                folders[0].imageUri = firstVideo
            }
            if ((allFolders[0] is FolderContent && (allFolders[0] as FolderContent).folderNameStringId == R.string.all_images) && (allFolders[1] as FolderContent).folderNameStringId == R.string.all_videos) {
                folders[1].imageUri = firstVideo
            }
        } else if (allPictureFolders.isEmpty() && allVideoFolders.isNotEmpty()) {
            allFolders.addAll(allVideoFolders)
        } else if (allPictureFolders.isNotEmpty() && allVideoFolders.isEmpty()) {
            allFolders.addAll(allPictureFolders)
        }
        _foldersData.postValue(folders)
    }
}