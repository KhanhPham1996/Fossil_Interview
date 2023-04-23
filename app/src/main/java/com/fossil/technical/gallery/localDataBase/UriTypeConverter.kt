package com.fossil.technical.gallery.localDataBase

import android.net.Uri
import androidx.room.TypeConverter

class UriTypeConverter {
    @TypeConverter
    fun uriToString(uri: Uri): String = uri.toString()

    @TypeConverter
    fun stringToUri(uriString: String): Uri = Uri.parse(uriString)
}