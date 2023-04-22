package com.fossil.technical.gallery.extention

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

 fun FragmentActivity.requestGalleryPermission(  isPermissionGranted: (Boolean) -> Unit) {

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
    return    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_MEDIA_IMAGES)
    }
    else{
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

    }
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

