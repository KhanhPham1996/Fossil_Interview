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
import com.fossil.technical.gallery.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class ListMediaViewModel @Inject constructor(private val galleryRepository: GalleryRepository) : BaseViewModel() {


    private val _state = MutableStateFlow<ViewState>(ViewState.DoneLoading)
    val state: Flow<ViewState>
        get() = _state

    private val _event = Channel<ViewEvent>()
    val event = _event.receiveAsFlow()

    private var _filterType: String = FILTER_TYPE_ALL

    var filterType: String
        get() = _filterType
        set(value){
            _filterType = value
        }


    private val currentListMediaFile = mutableSetOf<MediaFile>()

    fun fetchFiles(context: Context) {
        execute {
            if(filterType == FILTER_TYPE_ALL){
                currentListMediaFile.clear()
                galleryRepository.loadFileFromDevice().collect {
                    currentListMediaFile.add(it)
                    _event.send(ViewEvent.ShowImage(currentListMediaFile.toList()))
                }
            }
            else{
                _event.send(ViewEvent.ShowImage(galleryRepository.getFavoriteFile()))
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



    sealed class ViewState {
        object DoneLoading : ViewState()
        object Loading : ViewState()

    }

    sealed class ViewEvent {
        class ShowImage(val listMediaFile: List<MediaFile>) : ViewEvent()
        object GetImage : ViewEvent()
        object RequestPermission : ViewEvent()

    }
    companion object {
        const val FILTER_TYPE_ALL ="ALL"
        const val FILTER_TYPE_FAVORITE ="FAVORITE"
    }

}
