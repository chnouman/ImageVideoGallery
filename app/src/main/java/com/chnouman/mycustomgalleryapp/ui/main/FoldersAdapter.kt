package com.chnouman.mycustomgalleryapp.ui.main

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chnouman.imagevideogallery.models.FolderWithOneImage
import com.chnouman.imagevideogallery.models.MediaTypes
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FolderItemBinding
import com.chnouman.mycustomgalleryapp.utils.gone
import com.chnouman.mycustomgalleryapp.utils.picture
import com.chnouman.mycustomgalleryapp.utils.pictureFit
import com.chnouman.mycustomgalleryapp.utils.visible


class FoldersAdapter(
    private val videoList: MutableList<FolderWithOneImage>,
    private val onVideoItemClicked: (position: Int) -> Unit,
    private val onVideoItemLongClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<FoldersAdapter.VideoViewHolder>() {
    private val listItem = 0
    private val gridItem = 1

    private var isSwitchView = true

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {

        val binding: FolderItemBinding = if (viewType == listItem) {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.folder_item, parent, false
            )
        } else {
            //not separating layout of now
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.folder_item, parent, false
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

    override fun getItemCount() = videoList.size

    inner class VideoViewHolder(private val binding: FolderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                videoList[adapterPosition].apply {
                    if (mediaType === MediaTypes.VIDEO) {
                        play.visible()
                    } else {
                        play.gone()
                    }
                    videoPreview.picture(imageUri?:"")
                    binding.folderName.text = if (folderName.isNullOrBlank()) {
                        this.folderNameStringId?.let {
                            binding.folderName.context.getString(it)
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
                videoPreview.setOnLongClickListener {
                    onVideoItemLongClicked.invoke(adapterPosition)
                    return@setOnLongClickListener true
                }
                videoPreview.setOnClickListener { onVideoItemClicked.invoke(adapterPosition) }
                play.setOnClickListener { onVideoItemClicked.invoke(adapterPosition) }
            }
        }
    }

}