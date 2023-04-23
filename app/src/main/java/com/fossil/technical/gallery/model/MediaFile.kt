package com.fossil.technical.gallery.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.fossil.technical.gallery.localDataBase.UriTypeConverter
import com.fossil.technical.gallery.model.MediaFile.Companion.TABLE_NAME
import kotlinx.android.parcel.Parcelize

@TypeConverters(UriTypeConverter::class)
@Entity(tableName = TABLE_NAME)
@Parcelize
data class MediaFile(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "path")
    val path: String,

    @ColumnInfo(name = "dateCreated")
    val dateCreated: String,

    @ColumnInfo(name = "fileURI")
    val fileURI: Uri,

    @ColumnInfo(name = "mediaType")
    val mediaType: Int,
): Parcelable {
    companion object{
        const val TABLE_NAME = "MediaFileTable"

        object MEDIA_TYPE {
            const val IMAGE_TYPE: Int =0
            const val VIDEO_TYPE: Int =1
            const val UN_KNOW: Int =-1
        }

    }
}