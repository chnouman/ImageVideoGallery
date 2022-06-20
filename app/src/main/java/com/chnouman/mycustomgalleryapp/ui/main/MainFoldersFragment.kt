package com.chnouman.mycustomgalleryapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chnouman.imagevideogallery.Utils.getBucketId
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FragmentMainBinding
import com.chnouman.mycustomgalleryapp.backend.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFoldersFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private var foldersAdapter: FoldersAdapter? = null
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
    }

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

    private fun setupObserver() {
        viewModel.foldersData.observe(this) { folders ->
            for (folder in folders) {
                Log.d("TEST", "getFoldersV: ${folder.bucketId}")
            }
            foldersAdapter = FoldersAdapter(folders, { position ->
                findNavController().navigate(
                    MainFoldersFragmentDirections.actionMainFragmentToDetailFragment(
                        folders[position].bucketId
                    )
                )
            }, {
                //onLong press
            })
            binding.videoFolderSelector.adapter = foldersAdapter
        }
    }

    private fun setupFolderSelector() {
        binding.apply {
            switchView.setOnClickListener {
                foldersAdapter?.let {
                    //not doing logic for storing user preference for style at the moment
                    val isSwitched: Boolean = it.toggleItemViewType()
                    videoFolderSelector.layoutManager =
                        if (isSwitched) {
                            switchView.setImageResource(R.drawable.ic_grid)
                            GridLayoutManager(
                                requireContext(),
                                2
                            )
                        } else {
                            switchView.setImageResource(R.drawable.ic_list)
                            LinearLayoutManager(requireContext())
                        }
                    it.notifyDataSetChanged()
                }
            }
            videoFolderSelector.apply {
                hasFixedSize()
                setHasFixedSize(true)
                setItemViewCacheSize(20)
                isDrawingCacheEnabled = true
                drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
                layoutManager =
                    GridLayoutManager(requireContext(), 2)
            }
        }
        viewModel.getFolders()
    }
}