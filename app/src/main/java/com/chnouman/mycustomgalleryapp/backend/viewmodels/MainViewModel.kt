package com.chnouman.mycustomgalleryapp.backend.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chnouman.imagevideogallery.Utils
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.imagevideogallery.models.*
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.backend.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val galleryRepository: GalleryRepository) :
    ViewModel() {

    private val _foldersData = MutableLiveData<MutableList<FolderWithOneImage>>()
    val foldersData: LiveData<MutableList<FolderWithOneImage>>
        get() = _foldersData

    private val _totalImages = MutableLiveData<Pair<Int, Int>>()
    val totalImages: LiveData<Pair<Int, Int>>
        get() = _totalImages

/*    private val _totalVideos = MutableLiveData<Int>()
    val totalVideos: LiveData<Int>
        get() = _totalVideos*/

    fun getFolders() {
        Log.d("TEST", "getFoldersCall: ")
        viewModelScope.launch {
            val allVideoFolders: MutableList<VideoFolderContent> = galleryRepository
                .getAllVideoFolders(VideoGet.externalContentUri)
            val allPictureFolders: MutableList<PictureFolderContent> =
                galleryRepository.getAllPictureFolders()
            //check if we have some content to show
            val allFoldersWithOneImage = mutableListOf<FolderWithOneImage>().apply {
                val totalImagesCount = Utils.getTotalImagesCount(allPictureFolders)
                val totalVideosCount = Utils.getTotalVideosCount(allVideoFolders)
                if (allPictureFolders.isNotEmpty()) {
                    add(
                        FolderWithOneImage(
                            folderNameStringId = R.string.all_images,
                            bucketId = -1,
                            total = Utils.getTotalImagesCount(allPictureFolders),
                            mediaType = MediaTypes.PICTURE
                        )
                    )
                }
                if (allVideoFolders.isNotEmpty()) {
                    add(
                        FolderWithOneImage(
                            folderNameStringId = R.string.all_videos,
                            bucketId = -2,
                            total = Utils.getTotalVideosCount(allVideoFolders),
                            mediaType = MediaTypes.VIDEO
                        )
                    )
                }
                _totalImages.value = Pair(totalImagesCount, totalVideosCount)
            }


            if (allVideoFolders.isNotEmpty() || allPictureFolders.isNotEmpty()) {

                combinePicturesAndVideos(
                    allFoldersWithOneImage,
                    allVideoFolders,
                    allPictureFolders
                )
            } else if (allPictureFolders.isEmpty() && allVideoFolders.isNotEmpty()) {
                //set image for first folder also
                allFoldersWithOneImage.addAll(allVideoFolders.map {
                    FolderWithOneImage(
                        it.videoFiles[0].videoUri, it.folderName, it.videoFiles.size,
                        MediaTypes.VIDEO, bucketId = it.bucketId
                    )
                })
            } else if (allPictureFolders.isNotEmpty() && allVideoFolders.isEmpty()) {
                //set image for first folder also
                allFoldersWithOneImage.addAll(allPictureFolders.map {
                    Log.d("TEST", "getFolders: ${it.photos.size} ${it.folderName}")
                    FolderWithOneImage(
                        it.photos[0].photoUri, it.folderName, it.photos.size,
                        MediaTypes.PICTURE, bucketId = it.bucketId
                    )
                })
            }
            _foldersData.postValue(allFoldersWithOneImage)
        }
    }

    private fun combinePicturesAndVideos(
        allFoldersWithOneImage: MutableList<FolderWithOneImage>,
        allVideoFolders: MutableList<VideoFolderContent>,
        allPictureFolders: MutableList<PictureFolderContent>
    ) {
        val folders = mutableListOf<FolderContent>()
        val resultFolder = mutableListOf<FolderWithOneImage>()
        //combine the folders
        val combinedBucketIds = mutableListOf<Int>()
        for (videosFolderContent in allVideoFolders) {
            var videoPictureFolder = false
            lateinit var videoPictureFolderContent: VideoPictureFolderContent
            for (pictureFolder in allPictureFolders) {
                if (videosFolderContent.bucketId == pictureFolder.bucketId
                ) {
                    //combine the data
                    videoPictureFolderContent = VideoPictureFolderContent(
                        videosFolderContent.folderPath, videosFolderContent.folderName,
                        videosFolderContent.videoFiles, pictureFolder.photos
                    )
                    videoPictureFolderContent.bucketId = videosFolderContent.bucketId
                    combinedBucketIds.add(videosFolderContent.bucketId)
                    videoPictureFolder = true
                    continue
                }
            }
            if (videoPictureFolder) {
                folders.add(videoPictureFolderContent)
            } else {
                //not found
                folders.add(videosFolderContent)
            }
        }

        //make sure all the picture folders also added
        for (pictureFolder in allPictureFolders) {
            var isFoundAsCombine = false
            for (bucketId in combinedBucketIds) {
                if (pictureFolder.bucketId == bucketId) {
                    isFoundAsCombine = true
                    continue
                }
            }
            if (!isFoundAsCombine) {
                folders.add(pictureFolder)
            }
        }
        var firstImage = ""
        var firstVideo = ""
        for (i in folders.indices) {
            val currentFolder = folders[i]
            val path: String? = Utils.getPath(currentFolder)
            if (currentFolder is PictureFolderContent && firstImage.isEmpty()) {
                firstImage = path ?: ""
            } else if (currentFolder is VideoFolderContent && firstVideo.isEmpty()) {
                firstVideo = path ?: ""
            }
            val folderName = Utils.getFolderName(folders[i])
            //get count
            val totalCount = when (folderName) {
                "" -> {
                    if (Utils.getFolderNameStringId(folders[i]) == R.string.all_images) {
                        Utils.getTotalImagesCount(allPictureFolders)
                    } else if (Utils.getFolderNameStringId(folders[i]) == R.string.all_videos) {
                        Utils.getTotalVideosCount(allVideoFolders)
                    } else {
                        0
                    }
                }
                else -> {
                    Log.d(
                        "TEST",
                        "combinePicturesAndVideos: total  = ${Utils.getTotal(folders[i])} ${folders[i]}"
                    )
                    Utils.getTotal(folders[i])
                }
            }
            Log.d("TEST", "getFoldersBucket: $folderName ${Utils.getBucketId(folders[i])}")
            resultFolder.add(
                FolderWithOneImage(
                    path ?: "",
                    folderName ?: "",
                    totalCount,
                    Utils.getMediaType(folders[i]),
                    //check if folder name is null than set the folderNameStringId
                    if (folderName.isNullOrBlank()) Utils.getFolderNameStringId(folders[i]) else -1,
                    bucketId = Utils.getBucketId(folders[i])
                )
            )
        }
        allFoldersWithOneImage.addAll(resultFolder)
        setAllFolderImages(allFoldersWithOneImage, firstImage, firstVideo)
    }

    private fun setAllFolderImages(
        allFoldersWithOneImage: MutableList<FolderWithOneImage>,
        firstImage: String,
        firstVideo: String
    ) {
        //set path for all images and all videos here
        //check if gallery is for all images
        val firstFolder = allFoldersWithOneImage[0]
        if (firstFolder.folderNameStringId == R.string.all_images) {
            allFoldersWithOneImage[0].imageUri = firstImage
        }
        if (firstFolder.folderNameStringId == R.string.all_videos) {
            allFoldersWithOneImage[0].imageUri = firstVideo
        }
        if ((firstFolder.folderNameStringId == R.string.all_images) && allFoldersWithOneImage[1].folderNameStringId == R.string.all_videos) {
            allFoldersWithOneImage[1].imageUri = firstVideo
        }
    }
}

