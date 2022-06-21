package com.chnouman.mycustomgalleryapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.viewmodels.main.MainViewModel
import com.chnouman.mycustomgalleryapp.databinding.FragmentMainBinding
import com.chnouman.mycustomgalleryapp.utils.gone
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
        viewModel.totalImages.observe(this) {
            binding.totalStatsTV.text = getString(R.string.dummy_stats, it.first, it.second)
        }
        viewModel.foldersData.observe(this) { folders ->
            binding.progressBar.gone()
            foldersAdapter = FoldersAdapter(folders, { position ->
                val folderWithOneImage = folders[position]
                folderWithOneImage.apply {
                    findNavController().navigate(
                        MainFoldersFragmentDirections.actionMainFragmentToDetailFragment(
                            bucketId, folderName ?: folderNameStringId?.let { getString(it) } ?: ""
                        )
                    )
                }
            }, {
                //onLong press
            })
            binding.videoFoldersRV.adapter = foldersAdapter
        }
    }

    private fun setupFolderSelector() {
        binding.apply {
            viewModel.getListViewMode()
            switchIV.setOnClickListener {
                foldersAdapter?.let {
                    //not doing logic for storing user preference for style at the moment
                    val isSwitched: Boolean = it.toggleItemViewType()
                    videoFoldersRV.layoutManager =
                        if (isSwitched) {
                            viewModel.setListViewMode(0)
                            switchIV.setImageResource(R.drawable.ic_grid)
                            GridLayoutManager(
                                requireContext(),
                                2
                            )
                        } else {
                            viewModel.setListViewMode(1)
                            switchIV.setImageResource(R.drawable.ic_list)
                            LinearLayoutManager(requireContext())
                        }
                    it.notifyDataSetChanged()
                }
            }
            videoFoldersRV.apply {
                hasFixedSize()
                setHasFixedSize(true)
                setItemViewCacheSize(20)
                isDrawingCacheEnabled = true
                drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
                layoutManager =
                    GridLayoutManager(requireContext(), 2)
            }
        }
        binding.progressBar.gone()
        viewModel.getFolders()
    }
}