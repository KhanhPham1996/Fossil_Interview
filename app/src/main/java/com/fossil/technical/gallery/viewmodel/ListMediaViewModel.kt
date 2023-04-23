package com.fossil.technical.gallery.viewmodel

import android.content.ContentResolver
import android.content.ContentResolver.*
import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.fossil.technical.gallery.model.MediaFile
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.IMAGE_TYPE
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.UN_KNOW
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.VIDEO_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*


class ListMediaViewModel : BaseViewModel() {


    private val _state = MutableStateFlow<ViewState>(ViewState.DoneLoading)
    val state: Flow<ViewState>
        get() = _state

    private val _event = Channel<ViewEvent>()
    val event = _event.receiveAsFlow()


    private val currentListMediaFile = mutableSetOf<MediaFile>()

    fun loadImageFromDevice(context: Context) {
        execute {
            loadImages(context).collect {
                currentListMediaFile.add(it)
                _event.send(ViewEvent.ShowImage(currentListMediaFile.toList()))
            }
        }
    }

    fun permissionDenied() {

    }

    fun requestStoragePermission() {
        execute {
            _event.send(ViewEvent.RequestPermission)
        }
    }

    private suspend fun loadImages(context: Context): Flow<MediaFile> =
        flow {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATA,
            )
            val selection =
                "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
            val selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
            val queryArgs = Bundle().apply {
                putInt(QUERY_ARG_SORT_DIRECTION, QUERY_SORT_DIRECTION_DESCENDING)
                putString(QUERY_ARG_SORT_COLUMNS, MediaStore.Files.FileColumns.DATE_ADDED)
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                putStringArray(
                    ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                    selectionArgs
                )
            }
            val cursor = context.contentResolver.query(uri, projection, queryArgs, null)
            cursor?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val pathColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val mediaTypeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val dateCreatedColumnIndex =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val path = cursor.getString(pathColumnIndex)
                    val dateCreated = cursor.getString(dateCreatedColumnIndex)
                    val mediaType = cursor.getInt(mediaTypeIndex)

                    val imageUri = ContentUris.withAppendedId(uri, id)
                    val image = MediaFile(
                        id = id,
                        name = name,
                        path = path,
                        dateCreated = dateCreated,
                        fileURI = imageUri,
                        mediaType = when (mediaType) {
                            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> IMAGE_TYPE
                            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> VIDEO_TYPE
                            else -> {
                                UN_KNOW
                            }

                        }
                    )
                    emit(image)
                }
                cursor.close()

            }

        }.flowOn(Dispatchers.IO)


    sealed class ViewState {
        object DoneLoading : ViewState()
        object Loading : ViewState()

    }

    sealed class ViewEvent {
        class ShowImage(val listMediaFile: List<MediaFile>) : ViewEvent()
        object GetImage : ViewEvent()
        object RequestPermission : ViewEvent()

    }

}
