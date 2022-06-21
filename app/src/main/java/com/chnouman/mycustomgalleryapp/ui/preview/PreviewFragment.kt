package com.chnouman.mycustomgalleryapp.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        binding.apply {
            backIconIV.setOnClickListener { findNavController().popBackStack() }
            picturesVP.apply {
                offscreenPageLimit = 3
                val adapter = PicturePagerAdapter()
                this.adapter = adapter
                setCurrentItem(args.position, true)
                binding.counterTV.text =
                    "${ args.position+1}/" + args.pictures.size
                this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        binding.counterTV.text =
                            "${ position+1}/" + args.pictures.size
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                    }
                })
            }
        }
    }

    private inner class PicturePagerAdapter : PagerAdapter() {
        @NonNull
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val binding: PicturePagerItemBinding =
                PicturePagerItemBinding.inflate(
                    LayoutInflater.from(container.context),
                ).apply {
                    args.pictures[position].photoUri?.let {
                        pictureIV.pictureFit(it)
                    }
                    (container as ViewPager).addView(pictureIV)
                }
            return binding.pictureIV
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