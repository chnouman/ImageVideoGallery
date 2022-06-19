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

                vidZone.apply {
                    setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    requestFocus()
                    seeker.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    start()
                    startUpdatingCallbackWithPosition()
                    play.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
                }
            }
        }
        val handler = Handler()
        val run = Runnable { binding.playerParent.gone() }
        handler.postDelayed(run, 2000)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViews(page: View) {
        binding.apply {

            control.setOnClickListener {
                playerParent.let {
                    if (it.isGone()) {
                        it.visible()
                    } else {
                        it.gone()
                    }
                }
            }
            rewind.setOnClickListener {
                var current = vidZone.currentPosition
                //rewind 10 seconds behind
                current = if (current > 10000) {
                    current - 10000
                } else {
                    0
                }
                vidZone.seekTo(current)
                seeker.progress = current
            }
            forward.setOnClickListener {
                var current = vidZone.currentPosition
                //forward 10 seconds ahead
                val max = args.allVideos[currentVideoPosition].videoDuration.toInt()
                val diff = max - current
                current = if (diff > 10000) {
                    current + 10000
                } else {
                    max
                }
                vidZone.seekTo(current)
                seeker.progress = current
            }
            previous.setOnClickListener { playPrevious() }
            next.setOnClickListener { playNext() }
            play.setOnClickListener {
                vidZone.let {
                    if (it.isPlaying) {
                        it.pause()
                        play.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
                    } else {
                        it.start()
                        play.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
                    }
                }
            }
            duration.text = convertDuration(args.allVideos[currentVideoPosition].videoDuration)
            seeker.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                var userSelectedPosition = 0
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        userSelectedPosition = progress
                    }
                    binding.progress.text = milliSecondsToTimer(progress.toLong())
                    seekBar.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    vidZone.seekTo(userSelectedPosition)
                }
            })
            vidZone.setOnCompletionListener {
                vidZone.stopPlayback()
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
                    vidZone.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seeker.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    duration.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    vidZone.start()
                    startUpdatingCallbackWithPosition()
                } else {
                    currentVideoPosition += 1
                    vidZone.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seeker.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    duration.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    vidZone.start()
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
                    vidZone.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seeker.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    duration.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    vidZone.start()
                    startUpdatingCallbackWithPosition()
                } else {
                    currentVideoPosition -= 1
                    vidZone.setVideoURI(Uri.parse(allVideos[currentVideoPosition].videoUri))
                    seeker.max = allVideos[currentVideoPosition].videoDuration.toInt()
                    duration.text = convertDuration(allVideos[currentVideoPosition].videoDuration)
                    vidZone.start()
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
            binding.seeker.progress = 0
        }
    }

    private fun updateProgressCallbackTask() {
        binding.apply {
            val currentPosition = vidZone.currentPosition
            seeker.progress = currentPosition
        }
    }
}