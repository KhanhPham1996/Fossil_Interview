package com.fossil.technical.gallery.model

import android.net.Uri

data class ImageInDevice(
    val id: Long,
    val name: String,
    val path: String,
    val dateCreated: String,
    val imageUri: Uri
)