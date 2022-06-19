package com.chnouman.mycustomgalleryapp.ui.preview

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chnouman.imagevideogallery.models.PictureContent
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FragmentPreviewBinding
import com.chnouman.mycustomgalleryapp.utils.ZoomOutPageTransformer

class PreviewFragment : Fragment() {
    private var allpics: ArrayList<PictureContent>? = null
    private var currentPosition = 0
    lateinit var binding: FragmentPreviewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.picturePager.apply {

            offscreenPageLimit = 3
            setPageTransformer(true, ZoomOutPageTransformer())
            val adapter = PicturePagerAdapter()
            this.adapter = adapter
            setCurrentItem(currentPosition, true)
        }
    }

    private inner class PicturePagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater =
                container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val pictureView = inflater.inflate(R.layout.picture_pager_item, null)
            val picturezone = pictureView.findViewById<ImageView>(R.id.picture_zone)
            Glide.with(activity!!)
                .load(Uri.parse(allpics!![position].photoUri))
                .apply(RequestOptions().fitCenter())
                .into(picturezone)
            (container as ViewPager).addView(pictureView)
            return pictureView
        }

        override fun getCount(): Int {
            return allpics!!.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }

        override fun destroyItem(containerCollection: ViewGroup, position: Int, view: Any) {
            (containerCollection as ViewPager).removeView(view as View)
        }
    }
}