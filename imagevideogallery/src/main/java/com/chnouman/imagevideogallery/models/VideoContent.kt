package com.chnouman.imagevideogallery.models

import android.os.Parcel
import android.os.Parcelable

class VideoContent() : Parcelable {
    var videoId: Long = 0
    var videoName: String? = null
    var path: String? = null
    var videoDuration: Long = 0
    var videoSize: Long = 0
    var videoUri: String? = null
    var album: String? = null
    var artist: String? = null

    constructor(parcel: Parcel) : this() {
        videoId = parcel.readLong()
        videoName = parcel.readString()
        path = parcel.readString()
        videoDuration = parcel.readLong()
        videoSize = parcel.readLong()
        videoUri = parcel.readString()
        album = parcel.readString()
        artist = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(videoId)
        parcel.writeString(videoName)
        parcel.writeString(path)
        parcel.writeLong(videoDuration)
        parcel.writeLong(videoSize)
        parcel.writeString(videoUri)
        parcel.writeString(album)
        parcel.writeString(artist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoContent> {
        override fun createFromParcel(parcel: Parcel): VideoContent {
            return VideoContent(parcel)
        }

        override fun newArray(size: Int): Array<VideoContent?> {
            return arrayOfNulls(size)
        }
    }
}