package com.chnouman.mycustomgalleryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chnouman.imagevideogallery.models.PictureContent
import com.chnouman.imagevideogallery.models.VideoContent
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.utils.gone
import com.chnouman.mycustomgalleryapp.utils.visible

class DetailAdapter(
    private val videoActivity: Context,
    private val videoList: ArrayList<Any>,
    private val onVideoItemClicked: (position: Int) -> Unit,
    private val onVideoItemLongClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<DetailAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(videoActivity)
        val itemView = inflater.inflate(R.layout.detail_item, null, false)
        return VideoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, OnLongClickListener {
        //define views
        var preview: ImageView
        var play: ImageButton

        fun bind() {
            val video = videoList[adapterPosition]

            val path = when (video) {
                is VideoContent -> {
                    play.visible()
                    video.path
                }
                is PictureContent -> {
                    play.gone()
                    video.picturePath
                }
                else -> {
                    ""
                }
            }
            Glide.with(videoActivity)
                .load(path)
                .apply(RequestOptions().centerCrop())
                .into(preview)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onVideoItemClicked.invoke(adapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            onVideoItemLongClicked.invoke(adapterPosition)
            return false
        }

        init {
            //instantiate views
            preview = itemView.findViewById(R.id.video_preview)
            play = itemView.findViewById(R.id.play)
            preview.setOnLongClickListener(this)
            play.setOnClickListener(this)
        }
    }
}