package com.chnouman.mycustomgalleryapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chnouman.imagevideogallery.ImageVideoGallery
import com.chnouman.imagevideogallery.Utils.getBucketId
import com.chnouman.imagevideogallery.Utils.getFolderName
import com.chnouman.imagevideogallery.Utils.getMediaType
import com.chnouman.imagevideogallery.Utils.getPath
import com.chnouman.imagevideogallery.Utils.getTotal
import com.chnouman.imagevideogallery.Utils.getTotalImagesCount
import com.chnouman.imagevideogallery.Utils.getTotalVideosCount
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.imagevideogallery.models.*
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.adapters.FoldersAdapter
import com.chnouman.mycustomgalleryapp.databinding.FragmentMainBinding
import com.chnouman.mycustomgalleryapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels()
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
        setupObserver()
        setupFolderSelector()
    }

    private fun setupObserver() {
        viewModel.foldersData.observe(viewLifecycleOwner) { folders ->
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
                            getBucketId(folders[position])
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
        viewModel.getFolders()
    }
}