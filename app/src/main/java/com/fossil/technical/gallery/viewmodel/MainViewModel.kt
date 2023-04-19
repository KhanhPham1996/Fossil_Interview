package com.fossil.technical.gallery.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow

class MainViewModel : BaseViewModel() {


    private val _state = MutableStateFlow<ViewState>(ViewState.Loading)
    val state: Flow<ViewState>
        get() = _state

    private val _event = Channel<ViewEvent>()
    val event =_event.consumeAsFlow()

    fun loadImage(context: Context) {

    }

    fun permissionDenied() {

    }

    fun requestStoragePermission() {
        execute {
            _event.send(ViewEvent.RequestPermission)
        }
    }

    sealed class ViewState  {
        object DoneLoading : ViewState()
        object Loading : ViewState()

    }

    sealed class ViewEvent {
        object ShowImage: ViewEvent()
        object GetImage: ViewEvent()
        object RequestPermission: ViewEvent()

    }

}
