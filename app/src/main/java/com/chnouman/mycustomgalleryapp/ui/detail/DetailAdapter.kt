package com.chnouman.mycustomgalleryapp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chnouman.imagevideogallery.models.PictureContent
import com.chnouman.imagevideogallery.models.VideoContent
import com.chnouman.mycustomgalleryapp.R
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
        val binding: DetailItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.detail_item, parent, false
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
                        play.visible()
                        video.videoUri
                    }
                    is PictureContent -> {
                        play.gone()
                        video.photoUri
                    }
                    else -> {
                        ""
                    }
                }
                path?.let {
                    videoPreview.picture(it)
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
                videoPreview.apply {
                    setOnLongClickListener(this@VideoViewHolder)
                    videoPreview.setOnClickListener(this@VideoViewHolder)
                }
                play.apply {
                    setOnClickListener(this@VideoViewHolder)
                    setOnLongClickListener(this@VideoViewHolder)
                }
            }
        }
    }
}