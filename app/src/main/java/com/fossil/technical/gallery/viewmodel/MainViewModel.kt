package com.fossil.technical.gallery.viewmodel

import android.content.ContentResolver
import android.content.ContentResolver.*
import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.fossil.technical.gallery.model.ImageInDevice
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow


class MainViewModel : BaseViewModel() {


    private val _state = MutableStateFlow<ViewState>(ViewState.Loading)
    val state: Flow<ViewState>
        get() = _state

    private val _event = Channel<ViewEvent>()
    val event = _event.consumeAsFlow()

    fun loadImageFromDevice(context: Context) {
        execute {
            loadImages(context, 0, 10).collect {
                val a = it
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

    private suspend fun loadImages(context: Context, offset: Int, limit: Int): Flow<ImageInDevice> =
        flow {
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA,
                )

            val queryArgs = Bundle().apply {
                putInt(QUERY_ARG_OFFSET, offset)
                putInt(QUERY_ARG_LIMIT, limit)
                putInt(QUERY_ARG_SORT_DIRECTION, QUERY_SORT_DIRECTION_DESCENDING)
                putString(QUERY_ARG_SORT_COLUMNS, MediaStore.Images.Media.DATE_TAKEN)
            }


            val cursor = context.contentResolver.query(uri, projection, queryArgs, null)

            cursor?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val pathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val dateCreatedColumnIndex =
                    cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val path = cursor.getString(pathColumnIndex)
                    val dateCreated = cursor.getString(dateCreatedColumnIndex)
                    val imageUri = ContentUris.withAppendedId(uri, id)
                    val image = ImageInDevice(
                        id = id,
                        name = name,
                        path = path,
                        dateCreated = dateCreated,
                        imageUri = imageUri
                    )
                    emit(image)
                }
                cursor.close()
            }
        }

    sealed class ViewState {
        object DoneLoading : ViewState()
        object Loading : ViewState()

    }

    sealed class ViewEvent {
        object ShowImage : ViewEvent()
        object GetImage : ViewEvent()
        object RequestPermission : ViewEvent()

    }

}
