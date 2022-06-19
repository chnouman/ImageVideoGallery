package com.chnouman.imagevideogallery.models

import android.os.Parcel
import android.os.Parcelable

class PictureContent() : Parcelable {
    var pictureName: String? = null
    var picturePath: String? = null
    var pictureSize: Long? = null
    var photoUri: String? = null
    var pictureId = 0

    constructor(parcel: Parcel) : this() {
        pictureName = parcel.readString()
        picturePath = parcel.readString()
        pictureSize = parcel.readValue(Long::class.java.classLoader) as? Long
        photoUri = parcel.readString()
        pictureId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pictureName)
        parcel.writeString(picturePath)
        parcel.writeValue(pictureSize)
        parcel.writeString(photoUri)
        parcel.writeInt(pictureId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PictureContent> {
        override fun createFromParcel(parcel: Parcel): PictureContent {
            return PictureContent(parcel)
        }

        override fun newArray(size: Int): Array<PictureContent?> {
            return arrayOfNulls(size)
        }
    }
}