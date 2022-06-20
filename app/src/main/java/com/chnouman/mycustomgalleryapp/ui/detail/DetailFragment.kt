package com.chnouman.mycustomgalleryapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.chnouman.imagevideogallery.models.PictureContent
import com.chnouman.imagevideogallery.models.VideoContent
import com.chnouman.mycustomgalleryapp.databinding.ActivityDetailBinding
import com.chnouman.mycustomgalleryapp.backend.viewmodels.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: ActivityDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            titleTextView.text = args.folderName
            backIcon.setOnClickListener { findNavController().popBackStack() }
        }
        binding.videoRecycler.apply {
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            hasFixedSize()
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true

//            val numOfColumns = calculateNoOfColumns(240f)
            binding.videoRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        viewModel.getContent(args.bucketId)
    }

    private fun setupObserver() {
        viewModel.bucketContent.observe(this) { bucketContent ->
            setupUpAndDisplayVideos(bucketContent)
        }
    }

    private fun calculateNoOfColumns(
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    private fun setupUpAndDisplayVideos(bucketContent: MutableList<Any>) {

        val videoAdapter = DetailAdapter(bucketContent,
            { position ->
                //play video
                if (bucketContent[position] is VideoContent)
                    playVideo(position, bucketContent)
                if (bucketContent[position] is PictureContent)
                //show picture information
                    displayPictureInFragment(bucketContent, position)
            }
        ) { position ->
            //show video information
            showVideoInfo(bucketContent[position])
        }
        binding.videoRecycler.adapter = videoAdapter
    }

    private fun playVideo(position: Int, bucketContent: MutableList<Any>) {
        val videos = mutableListOf<VideoContent>()
        if (args.bucketId != -2) {
            videos.add(bucketContent[position] as VideoContent)
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToVideoPlayerFragment(
                    0,
                    videos.toTypedArray()
                )
            )
        } else {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToVideoPlayerFragment(
                    position,
                    (bucketContent as MutableList<VideoContent>).toTypedArray()
                )
            )
        }
    }

    private fun showVideoInfo(content: Any) {
        if (content is VideoContent) {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToVideoInfoFragment(
                    content
                )
            )
        } else findNavController().navigate(
            DetailFragmentDirections.actionDetailFragmentToPictureInfoFragment(
                content as PictureContent
            )
        )
    }

    private fun displayPictureInFragment(
        pictureList: MutableList<Any>,
        position: Int
    ) {
        val pictures = mutableListOf<PictureContent>()
        if (args.bucketId != -1 && pictures !is MutableList<PictureContent>) {
            pictures.add(pictureList[position] as PictureContent)
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToPreviewFragment(
                    0,
                    pictures.toTypedArray()
                )
            )
        } else {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToPreviewFragment(
                    position,
                    (pictureList as MutableList<PictureContent>).toTypedArray()
                )
            )
        }
    }
}