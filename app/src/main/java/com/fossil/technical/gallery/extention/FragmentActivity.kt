package com.fossil.technical.gallery.extention

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.AnimRes
import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

 fun Fragment.requestGalleryPermission(  isPermissionGranted: (Boolean) -> Unit) {

    if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        isPermissionGranted.invoke(true)
    } else {
        // Permission not yet granted, request it using the ActivityResultLauncher
        isPermissionGranted.invoke(false)
    }
}
fun Fragment.shouldShowGalleryPermissionRationale(): Boolean {
    return    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.READ_MEDIA_IMAGES)
    }
    else{
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
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
inline  fun <T , S : Flow<T>> Fragment.bindTo(flow: S, crossinline  action: (T) -> Unit) {
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

fun Fragment.replaceFragmentWithTransition(
    containerId: Int,
    fragment: Fragment,
    tag: String?,
    enter: Int,
    exit: Int,
    popEnter: Int,
    popExit: Int
) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction().setCustomAnimationsCompat(
        enter = enter,
        exit = exit,
        popEnter = popEnter,
        popExit = popExit
    )
    transaction.replace(containerId, fragment, tag)
    transaction.addToBackStack(fragment.tag)
    transaction.commit()
}
fun FragmentActivity.replaceFragmentWithNoTransition(
    containerId: Int,
    fragment: Fragment,
    tag: String?,
) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(containerId, fragment, tag)
    transaction.addToBackStack(fragment.tag)
    transaction.commit()
}

fun FragmentTransaction.setCustomAnimationsCompat(
    @AnimRes enter: Int,
    @AnimRes exit: Int,
    @AnimRes popEnter: Int,
    @AnimRes popExit: Int
): FragmentTransaction = apply {
    setCustomAnimations(enter, exit, popEnter, popExit)
}

