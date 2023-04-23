package com.fossil.technical.gallery.viewmodel

import com.fossil.technical.gallery.model.MediaFile
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*


class MediaDetailViewModel : BaseViewModel() {


    private val _state = MutableStateFlow<ViewState>(ViewState.DoneLoading)
    val state: Flow<ViewState>
        get() = _state

    private val _event = Channel<ViewEvent>()
    val event = _event.receiveAsFlow()


    private val currentListMediaFile = mutableSetOf<MediaFile>()

    fun showMediaToUI(mediaFile: MediaFile) {
        execute {
            _event.send(ViewEvent.ShowMediaFile(mediaFile))
        }
    }



    sealed class ViewState {
        object DoneLoading : ViewState()
        object Loading : ViewState()

    }

    sealed class ViewEvent {
        class ShowMediaFile(val mediaFile: MediaFile) : ViewEvent()
        object SaveOrDeleteFavorite : ViewEvent()
    }

}
