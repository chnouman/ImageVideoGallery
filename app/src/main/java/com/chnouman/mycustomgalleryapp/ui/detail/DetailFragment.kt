package com.chnouman.mycustomgalleryapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.chnouman.imagevideogallery.ImageVideoGallery.Companion.withPictureContext
import com.chnouman.imagevideogallery.ImageVideoGallery.Companion.withVideoContext
import com.chnouman.imagevideogallery.PictureGet
import com.chnouman.imagevideogallery.VideoGet.Companion.externalContentUri
import com.chnouman.imagevideogallery.VideoGet.Companion.internalContentUri
import com.chnouman.imagevideogallery.models.PictureContent
import com.chnouman.imagevideogallery.models.VideoContent
import com.chnouman.mycustomgalleryapp.adapters.DetailAdapter
import com.chnouman.mycustomgalleryapp.databinding.ActivityDetailBinding

class DetailFragment : Fragment() {
    private var allContent: ArrayList<Any> = ArrayList()
    private lateinit var binding: ActivityDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityDetailBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoRecycler.apply {
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            hasFixedSize()
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true

            val numOfColumns = calculateNoOfColumns(120f)
            binding.videoRecycler.layoutManager = GridLayoutManager(requireContext(), numOfColumns)
        }
        allContent.clear()
        when (args.bucketId) {
            -1 -> {//indicates All Images
                allContent.addAll(
                    withPictureContext(requireContext())
                        .getAllPictureContents(PictureGet.externalContentUri)
                )
                Toast.makeText(requireContext(), allContent.size.toString(), Toast.LENGTH_LONG)
                    .show()
                setupUpAndDisplayVideos()
            }
            -2 -> {//indicates All Videos
                allContent.addAll(
                    withVideoContext(requireContext())
                        .getAllVideoContent(externalContentUri)
                )
                Toast.makeText(requireContext(), allContent.size.toString(), Toast.LENGTH_LONG)
                    .show()
                setupUpAndDisplayVideos()
            }
            else -> {
                val allVideoContentByBucketId = withVideoContext(requireContext())
                    .getAllVideoContentByBucketId(args.bucketId)
                val allPictureContentByBucketId = withPictureContext(requireContext())
                    .getAllPictureContentByBucketId(args.bucketId)
                allContent.addAll(allVideoContentByBucketId)
                allContent.addAll(allPictureContentByBucketId)
                //TODO above two statements are not correct
                Toast.makeText(requireContext(), allContent.size.toString(), Toast.LENGTH_LONG)
                    .show()
                setupUpAndDisplayVideos()
            }
        }
    }

    private fun calculateNoOfColumns(
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    private fun setupUpAndDisplayVideos() {

        val videoAdapter = DetailAdapter(requireActivity(), allContent,
            { position ->
                //play video
                if (allContent[position] is VideoContent)
                    playVideo(position)
                if (allContent[position] is PictureContent)
                //show picture information
                    displayPictureInFragment(allContent as ArrayList<PictureContent>, position)
            }
        ) { position ->
            //show video information
            showVideoInfo(allContent[position])
        }
        binding.videoRecycler.adapter = videoAdapter
    }

    private fun playVideo(position: Int) {

        findNavController().navigate(
            DetailFragmentDirections.actionDetailFragmentToVideoPlayerFragment(
                position,
                (allContent as ArrayList<VideoContent>).toTypedArray()
            )
        )
    }

    private fun showVideoInfo(video: Any) {
        findNavController().navigate(
            DetailFragmentDirections.actionDetailFragmentToVideoInfoFragment(
                video as VideoContent
            )
        )
    }

    private fun displayPictureInFragment(
        pictureList: ArrayList<PictureContent>,
        position: Int
    ) {
        findNavController().navigate(
            DetailFragmentDirections.actionDetailFragmentToPreviewFragment(
                position,
                pictureList.toTypedArray()
            )
        )
    }

}