package com.chnouman.mycustomgalleryapp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chnouman.imagevideogallery.models.PictureContent
import com.chnouman.imagevideogallery.models.VideoContent
import com.chnouman.mycustomgalleryapp.databinding.DetailItemBinding
import com.chnouman.mycustomgalleryapp.utils.gone
import com.chnouman.mycustomgalleryapp.utils.picture
import com.chnouman.mycustomgalleryapp.utils.visible

class DetailAdapter(
    private val videoList: MutableList<Any>,
    private val onVideoItemClicked: (position: Int) -> Unit,
    private val onVideoItemLongClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<DetailAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding: DetailItemBinding = DetailItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    inner class VideoViewHolder(private val binding: DetailItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener, OnLongClickListener {

        fun bind() {
            binding.apply {
                val path = when (val video = videoList[adapterPosition]) {
                    is VideoContent -> {
                        playIB.visible()
                        video.videoUri
                    }
                    is PictureContent -> {
                        playIB.gone()
                        video.photoUri
                    }
                    else -> {
                        ""
                    }
                }
                path?.let {
                    videoPreviewIV.picture(it)
                }
                itemView.setOnClickListener(this@VideoViewHolder)
            }
        }

        override fun onClick(view: View) {
            onVideoItemClicked.invoke(adapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            onVideoItemLongClicked.invoke(adapterPosition)
            return false
        }

        init {
            binding.apply {
                videoPreviewIV.apply {
                    setOnLongClickListener(this@VideoViewHolder)
                    setOnClickListener(this@VideoViewHolder)
                }
                playIB.apply {
                    setOnClickListener(this@VideoViewHolder)
                    setOnLongClickListener(this@VideoViewHolder)
                }
            }
        }
    }
}