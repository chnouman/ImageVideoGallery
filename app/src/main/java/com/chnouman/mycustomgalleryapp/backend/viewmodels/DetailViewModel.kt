package com.chnouman.mycustomgalleryapp.backend.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chnouman.imagevideogallery.PictureGet
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.mycustomgalleryapp.backend.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val galleryRepository: GalleryRepository) :
    ViewModel() {

    private val _bucketContent = MutableLiveData<MutableList<Any>>()
    val bucketContent: LiveData<MutableList<Any>>
        get() = _bucketContent


    fun getContent(bucketId: Int) {
        viewModelScope.launch {

            val allContent = mutableListOf<Any>()
            when (bucketId) {
                -1 -> {//indicates All Images
                    allContent.addAll(
                        galleryRepository.getAllPictureContents(PictureGet.externalContentUri)
                    )
                }
                -2 -> {//indicates All Videos
                    allContent.addAll(
                        galleryRepository
                            .getAllVideoContent(VideoGet.externalContentUri)
                    )
                }
                else -> {
                    val allVideoContentByBucketId =
                        galleryRepository.getAllVideoContentByBucketId(bucketId)
                    val allPictureContentByBucketId = galleryRepository
                        .getAllPictureContentByBucketId(bucketId)
                    allContent.addAll(allVideoContentByBucketId)
                    allContent.addAll(allPictureContentByBucketId)
                    //TODO above two statements are not correct
                }
            }
            _bucketContent.postValue(allContent)
        }
    }
}
