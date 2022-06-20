package com.chnouman.mycustomgalleryapp.ui.detail.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FragmentPreviewBinding
import com.chnouman.mycustomgalleryapp.databinding.PicturePagerItemBinding
import com.chnouman.mycustomgalleryapp.utils.pictureFit

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
             val adapter = PicturePagerAdapter()
            this.adapter = adapter
            setCurrentItem(args.position, true)
            binding.counterTextView.text = "${args.position}/" + args.pictures.size
            this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    binding.counterTextView.text = "${position}/" + args.pictures.size
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        }
    }

    private inner class PicturePagerAdapter : PagerAdapter() {
        @NonNull
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val binding: PicturePagerItemBinding =
                DataBindingUtil.inflate<PicturePagerItemBinding?>(
                    LayoutInflater.from(container.context),
                    R.layout.picture_pager_item, null, false
                ).apply {
                    args.pictures[position].photoUri?.let {
                        pictureZone.pictureFit(it)
                    }
                    (container as ViewPager).addView(pictureZone)
                }
            return binding.pictureZone
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