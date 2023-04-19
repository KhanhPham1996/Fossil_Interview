package com.fossil.technical.gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel(
) : ViewModel() {

    protected inline fun execute(
        crossinline job: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch {
        job.invoke(this)
    }

}