package com.chnouman.mycustomgalleryapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chnouman.imagevideogallery.ImageVideoGallery
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.imagevideogallery.models.*
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.adapters.FoldersAdapter
import com.chnouman.mycustomgalleryapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFolderSelector()
    }

    private fun calculateNoOfColumns(
        columnWidthDp: Float
    ): Int { // For example column Width dp=180
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    private fun setupFolderSelector() {
        binding.videoFolderSelector.apply {
            hasFixedSize()
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            layoutManager =
                GridLayoutManager(requireContext(), 2)
        }

        val allVideoFolders: ArrayList<VideoFolderContent> =
            ImageVideoGallery.withVideoContext(requireContext())
                .getAllVideoFolders(VideoGet.externalContentUri)
        val allPictureFolders: ArrayList<PictureFolderContent> =
            ImageVideoGallery.withPictureContext(requireContext()).allPictureFolders
        //check if we have some content to show
        val allFolders = ArrayList<Any>().apply {
            if (allPictureFolders.isNotEmpty()) {
                add(PictureFolderContent("all", getString(R.string.all_images)))
            }
            if (allVideoFolders.isNotEmpty()) {
                add(VideoFolderContent("all", getString(R.string.all_videos)))
            }
        }
        val folders: ArrayList<FolderWithOneVideo> = ArrayList()
        if (allVideoFolders.isNotEmpty() || allPictureFolders.isNotEmpty()) {
            //combine the folders
            val combinedFolderNames = ArrayList<String>()
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
                    path = getPath(allFolders[i])
                    if (allFolders[i] is PictureFolderContent && firstImage.isEmpty()) {
                        firstImage = path ?: ""
                    } else if (allFolders[i] is VideoFolderContent && firstVideo.isEmpty()) {
                        firstVideo = path ?: ""
                    }
                }
                folders.add(
                    FolderWithOneVideo(
                        path ?: "",
                        getFolderName(allFolders[i]) ?: "",
                        getMediaType(allFolders[i])
                    )
                )
            }
            //set path for all images and all videos here
            //check if gallery is for all images
            if (allFolders[0] is PictureFolderContent) {
                folders[0].videoPath = firstImage
            }
            if (allFolders[0] is VideoFolderContent) {
                folders[0].videoPath = firstVideo
            }
            if (allFolders[0] is PictureFolderContent && allFolders[1] is VideoFolderContent) {
                folders[1].videoPath = firstVideo
            }
        } else if (allPictureFolders.isEmpty() && allVideoFolders.isNotEmpty()) {
            allFolders.addAll(allVideoFolders)
        } else if (allPictureFolders.isNotEmpty() && allVideoFolders.isEmpty()) {
            allFolders.addAll(allPictureFolders)
        }
        val selectorAdapter = FoldersAdapter(requireActivity(), folders, { position ->
            val bucketId =
                when (folders[position].folderName) {
                    getString(R.string.all_images) -> {
                        -1
                    }
                    getString(R.string.all_videos) -> {
                        -2
                    }
                    else -> {
                        getBucketId(allFolders[position])
                    }
                }
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(
                    bucketId
                )
            )
        }, {
            //onLong press
        })
        binding.videoFolderSelector.adapter = selectorAdapter
    }

    private fun getMediaType(o: Any?) =
        when (o) {
            is VideoFolderContent -> {
                MediaTypes.VIDEO
            }
            is PictureFolderContent -> MediaTypes.PICTURE
            is VideoPictureFolderContent -> MediaTypes.MIX
            else -> MediaTypes.VIDEO
        }


    private fun getBucketId(o: Any) =
        when (o) {
            is VideoFolderContent -> o.bucketId
            is PictureFolderContent -> o.bucketId
            is VideoPictureFolderContent -> o.bucketId
            else -> -1
        }


    private fun getFolderName(o: Any) =
        when (o) {
            is VideoFolderContent -> o.folderName
            is PictureFolderContent -> o.folderName
            is VideoPictureFolderContent -> o.folderName
            else -> ""
        }

    private fun getPath(o: Any) =
        when (o) {
            is VideoFolderContent -> o.videoFiles[0].videoUri
            is PictureFolderContent -> o.photos[0].photoUri
            is VideoPictureFolderContent -> o.photos?.get(0)?.photoUri ?: ""
            else -> ""
        }
}