package com.fossil.technical.gallery.extention

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun FragmentActivity.requestGalleryPermission( crossinline isPermissionGranted: (Boolean) -> Unit) {

    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        isPermissionGranted.invoke(true)
    } else {
        // Permission not yet granted, request it using the ActivityResultLauncher
        isPermissionGranted.invoke(false)
    }
}
fun FragmentActivity.shouldShowGalleryPermissionRationale(): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}

inline  fun <T , S : Flow<T>> FragmentActivity.bindTo(flow: S, crossinline  action: (T) -> Unit) {
    this.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { data ->
                data?.let {
                    action(it)
                }
            }
        }
    }
}

