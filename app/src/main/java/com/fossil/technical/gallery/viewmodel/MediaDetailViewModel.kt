package com.fossil.technical.gallery.viewmodel

import com.fossil.technical.gallery.model.MediaFile
import com.fossil.technical.gallery.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : BaseViewModel() {


    private val _state = MutableStateFlow<ViewState>(ViewState.DoneLoading)
    val state: Flow<ViewState>
        get() = _state

    private val _event = Channel<ViewEvent>()
    val event = _event.receiveAsFlow()


    private val currentListMediaFile = mutableSetOf<MediaFile>()

    fun showMediaToUI(mediaFile: MediaFile) {
        execute {
            _event.send(ViewEvent.ShowMediaFile(mediaFile))
            _event.send(ViewEvent.ShouldMarkAsFavorite(galleryRepository.isExistInFavorite(mediaFile)))


        }
    }

    fun addToFavorite(mediaFile: MediaFile) {
        execute {
            galleryRepository.addMediaFileToFavorite(mediaFile)
        }
    }

    fun removeFromFavorite(mediaFile: MediaFile) {
        execute {
            galleryRepository.removeMediaFileFromFavorite(mediaFile)
        }
    }


    sealed class ViewState {
        object DoneLoading : ViewState()
        object Loading : ViewState()

    }

    sealed class ViewEvent {
        class ShowMediaFile(val mediaFile: MediaFile) : ViewEvent()
        class ShouldMarkAsFavorite(val shouldMarkAsFavorite: Boolean) : ViewEvent()
    }

}
