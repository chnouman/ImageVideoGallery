package com.chnouman.mycustomgalleryapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chnouman.imagevideogallery.models.FolderWithOneVideo
import com.chnouman.imagevideogallery.models.MediaTypes
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FolderItemBinding
import com.chnouman.mycustomgalleryapp.utils.gone
import com.chnouman.mycustomgalleryapp.utils.visible


class FoldersAdapter(
    private val videoActivity: Context,
    private val videoList: ArrayList<FolderWithOneVideo>,
    private val onVideoItemClicked: (position: Int) -> Unit,
    private val onVideoItemLongClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<FoldersAdapter.VideoViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding: FolderItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.folder_item, parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) = holder.bind()


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
                    Glide.with(videoActivity)
                        .load(videoPath)
                        .apply(RequestOptions().centerCrop())
                        .into(videoPreview)
                    binding.folderName.text = folderName
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