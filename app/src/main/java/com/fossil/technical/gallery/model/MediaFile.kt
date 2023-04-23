package com.fossil.technical.gallery.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaFile(
    val id: Long,
    val name: String,
    val path: String,
    val dateCreated: String,
    val fileURI: Uri,
    val mediaType: Int,
): Parcelable {
    companion object{
        object MEDIA_TYPE {
            const val IMAGE_TYPE: Int =0
            const val VIDEO_TYPE: Int =1
            const val UN_KNOW: Int =-1
        }

    }
}