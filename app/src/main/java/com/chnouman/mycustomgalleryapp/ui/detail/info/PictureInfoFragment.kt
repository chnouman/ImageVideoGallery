package com.chnouman.mycustomgalleryapp.ui.detail.info


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

class PictureInfoFragment : DialogFragment() {
    private lateinit var binding: FragmentInfoBinding
    private val args: PictureInfoFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        binding = FragmentInfoBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            args.picture.apply {
                filename.text = pictureName
                fullpath.text = picturePath
                size.text = convertBytes(pictureSize!!)
                Glide.with(activity!!)
                    .load(picturePath)
                    .apply(
                        RequestOptions().placeholder(R.drawable.ic_launcher).centerCrop()
                    )
                    .into(pic)
            }
        }
    }
}