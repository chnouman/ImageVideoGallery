package com.chnouman.mycustomgalleryapp.ui.play

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
 import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.chnouman.imagevideogallery.MediaDataCalculator.convertDuration
import com.chnouman.imagevideogallery.MediaDataCalculator.milliSecondsToTimer
import com.chnouman.mycustomgalleryapp.R
import com.chnouman.mycustomgalleryapp.databinding.FragmentVideoPlayerBinding
import com.chnouman.mycustomgalleryapp.utils.gone
import com.chnouman.mycustomgalleryapp.utils.isGone
import com.chnouman.mycustomgalleryapp.utils.visible

class VideoPlayerFragment : Fragment() {
    private val args: VideoPlayerFragmentArgs by navArgs()
    private var mHandler: Handler? = null
    private var mSeekbarPositionUpdateTask: Runnable? = null
     private lateinit var binding: FragmentVideoPlayerBinding
    private var currentVideoPosition = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoPlayerBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentVideoPosition = args.position
        setUpViews(view)

        //Set MediaController  to enable play, pause, forward, etc options.
        //MediaController mediaController= new MediaController(getActivity());
        //mediaController.setAnchorView(playZone);
        //playZone.setMediaController(mediaController);
        binding.apply {
            args.apply {

                videoView.apply {
                    setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    requestFocus()
                    seekerSB.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    start()
                    startUpdatingCallbackWithPosition()
                    playIB.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
                }
            }
        }
        val handler = Handler()
        val run = Runnable { binding.playerParentCL.gone() }
        handler.postDelayed(run, 2000)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViews(page: View) {
        binding.apply {

            controlView.setOnClickListener {
                playerParentCL.let {
                    if (it.isGone()) {
                        it.visible()
                    } else {
                        it.gone()
                    }
                }
            }
            rewindTV.setOnClickListener {
                var current = videoView.currentPosition
                //rewind 10 seconds behind
                current = if (current > 10000) {
                    current - 10000
                } else {
                    0
                }
                videoView.seekTo(current)
                seekerSB.progress = current
            }
            forwardTV.setOnClickListener {
                var current = videoView.currentPosition
                //forward 10 seconds ahead
                val max = args.allVideos[currentVideoPosition].videoDuration.toInt()
                val diff = max - current
                current = if (diff > 10000) {
                    current + 10000
                } else {
                    max
                }
                videoView.seekTo(current)
                seekerSB.progress = current
            }
            previousIB.setOnClickListener { playPrevious() }
            nextIB.setOnClickListener { playNext() }
            playIB.setOnClickListener {
                videoView.let {
                    if (it.isPlaying) {
                        it.pause()
                        playIB.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
                    } else {
                        it.start()
                        playIB.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
                    }
                }
            }
            durationTV.text = convertDuration(args.allVideos[currentVideoPosition].videoDuration)
            seekerSB.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                var userSelectedPosition = 0
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        userSelectedPosition = progress
                    }
                    binding.progressTV.text = milliSecondsToTimer(progress.toLong())
                    seekBar.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    videoView.seekTo(userSelectedPosition)
                }
            })
            videoView.setOnCompletionListener {
                videoView.stopPlayback()
                stopUpdatingCallbackWithPosition()
                playNext()
            }
        }
    }

    private fun playNext() {
        binding.apply {
            args.apply {

                if (currentVideoPosition == allVideos.size - 1) {
                    currentVideoPosition = 0
                    videoView.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seekerSB.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    durationTV.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    videoView.start()
                    startUpdatingCallbackWithPosition()
                } else {
                    currentVideoPosition += 1
                    videoView.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seekerSB.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    durationTV.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    videoView.start()
                    startUpdatingCallbackWithPosition()
                }
            }
        }
    }

    private fun playPrevious() {
        binding.apply {
            args.apply {

                if (currentVideoPosition == 0) {
                    currentVideoPosition = allVideos.size - 1
                    videoView.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seekerSB.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    durationTV.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    videoView.start()
                    startUpdatingCallbackWithPosition()
                } else {
                    currentVideoPosition -= 1
                    videoView.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seekerSB.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    durationTV.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    videoView.start()
                    startUpdatingCallbackWithPosition()
                }
            }
        }
    }


    private fun startUpdatingCallbackWithPosition() {
        if (mHandler == null) {
            mHandler = Handler()
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = object : Runnable {
                override fun run() {
                    updateProgressCallbackTask()
                    mHandler!!.postDelayed(this, 1000)
                }
            }
            mHandler!!.post(mSeekbarPositionUpdateTask as Runnable)
        }
    }

    private fun stopUpdatingCallbackWithPosition() {
        if (mHandler != null) {
            mHandler!!.removeCallbacks(mSeekbarPositionUpdateTask!!)
            mHandler = null
            mSeekbarPositionUpdateTask = null
            binding.seekerSB.progress = 0
        }
    }

    private fun updateProgressCallbackTask() {
        binding.apply {
            val currentPosition = videoView.currentPosition
            seekerSB.progress = currentPosition
        }
    }
}