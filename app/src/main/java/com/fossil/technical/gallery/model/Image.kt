package com.fossil.technical.gallery.model

import android.net.Uri

data class MediaFile(
    val id: Long,
    val name: String,
    val path: String,
    val dateCreated: String,
    val imageUri: Uri,
    val mediaType: Int,
){
    companion object{
        object MEDIA_TYPE {
            const val IMAGE_TYPE: Int =0
            const val VIDEO_TYPE: Int =1
            const val UNKNOW: Int =-1
        }

    }
}