package com.chnouman.mycustomgalleryapp.ui.preview

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FragmentPreviewBinding
import com.chnouman.mycustomgalleryapp.utils.ZoomOutPageTransformer

class PreviewFragment : Fragment() {
     lateinit var binding: FragmentPreviewBinding
    private val args: PreviewFragmentArgs by navArgs()
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
            setCurrentItem(args.position, true)
        }
    }

    private inner class PicturePagerAdapter : PagerAdapter() {
        @NonNull
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
           /* val binding: PicturePagerItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(container.context),
                R.layout.picture_pager_item, null, false
            )*/
            val inflater =
                container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val pictureView = inflater.inflate(R.layout.picture_pager_item, null)
            val picturezone = pictureView.findViewById<ImageView>(R.id.picture_zone)

            Glide.with(requireActivity())
                .load(Uri.parse(args.pictures[position].photoUri))
                .apply(RequestOptions().fitCenter())
                .into(picturezone)
            (container as ViewPager).addView(picturezone)
            return picturezone
        }

        override fun getCount(): Int {
            return args.pictures.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }

        override fun destroyItem(containerCollection: ViewGroup, position: Int, view: Any) {
            (containerCollection as ViewPager).removeView(view as View)
        }
    }
}