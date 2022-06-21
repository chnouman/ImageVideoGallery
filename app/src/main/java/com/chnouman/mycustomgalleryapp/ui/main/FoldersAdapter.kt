package com.chnouman.mycustomgalleryapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chnouman.imagevideogallery.models.FolderWithOneImage
import com.chnouman.imagevideogallery.models.MediaTypes
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FolderItemBinding
import com.chnouman.mycustomgalleryapp.utils.gone
import com.chnouman.mycustomgalleryapp.utils.picture
import com.chnouman.mycustomgalleryapp.utils.visible


class FoldersAdapter(
    private val foldersList: MutableList<FolderWithOneImage>,
    private val onVideoItemClicked: (position: Int) -> Unit,
    private val onVideoItemLongClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<FoldersAdapter.VideoViewHolder>() {
    private val listItem = 0
    private val gridItem = 1

    private var isSwitchView = true

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {

        val binding: FolderItemBinding = if (viewType == listItem) {
            FolderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false,
            )
        } else {
            //not separating layout of now
            FolderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) = holder.bind()

    override fun getItemViewType(position: Int) = if (isSwitchView) {
        listItem
    } else {
        gridItem
    }

    fun toggleItemViewType(): Boolean {
        isSwitchView = !isSwitchView
        return isSwitchView
    }

    override fun getItemCount() = foldersList.size
    inner class VideoViewHolder(private val binding: FolderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                foldersList[adapterPosition].apply {
                    if (mediaType === MediaTypes.VIDEO) {
                        playIB.visible()
                    } else {
                        playIB.gone()
                    }
                    videoPreviewIV.picture(imageUri ?: "")
                    binding.folderNameTV.text = if (folderName.isNullOrBlank()) {
                        this.folderNameStringId?.let {
                            binding.folderNameTV.context.getString(it)
                        }
                    } else {
                        folderName
                    }
                    binding.totalImagesTV.text = total.toString()
                }
                itemView.setOnClickListener { onVideoItemClicked.invoke(adapterPosition) }
            }
        }

        init {
            binding.apply {
                videoPreviewIV.setOnLongClickListener {
                    onVideoItemLongClicked.invoke(adapterPosition)
                    return@setOnLongClickListener true
                }
                videoPreviewIV.setOnClickListener { onVideoItemClicked.invoke(adapterPosition) }
                playIB.setOnClickListener { onVideoItemClicked.invoke(adapterPosition) }
            }
        }
    }

}