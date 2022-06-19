package com.chnouman.mycustomgalleryapp.ui.info

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chnouman.imagevideogallery.MediaDataCalculator.convertBytes
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FragmentInfoBinding

class VideoInfoFragment : DialogFragment() {
    private val args: VideoInfoFragmentArgs by navArgs()
    private lateinit var binding: FragmentInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        binding = FragmentInfoBinding.inflate(
            inflater, container, false
        ).apply {
            filename.text = args.videoContent.videoName
            fullpath.text = args.videoContent.path
            size.text = convertBytes(args.videoContent.videoSize)
            Glide.with(requireActivity())
                .load(Uri.parse(args.videoContent.videoUri))
                .apply(
                    RequestOptions().placeholder(R.drawable.ic_launcher_foreground).centerCrop()
                )
                .into(pic)
        }
        return binding.root
    }
}