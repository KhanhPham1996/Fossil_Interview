package com.fossil.technical.gallery

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.fossil.technical.gallery.extention.bindTo
import com.fossil.technical.gallery.extention.requestGalleryPermission
import com.fossil.technical.gallery.extention.shouldShowGalleryPermissionRationale
import com.fossil.technical.gallery.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                mainViewModel.loadImage(this)
            } else {
                mainViewModel.permissionDenied()

            }
        }
    private val mainViewModel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        collectDataFromViewModel()
        requestStorageAndGetImage()
    }

    private fun requestStorageAndGetImage() {
        mainViewModel.requestStoragePermission()
    }

    private fun collectDataFromViewModel() {
        with(mainViewModel) {
            bindTo(this.state, ::bindViewStateChange)
            bindTo(this.event, ::bindViewEvent)
        }



    }
    private fun bindViewStateChange(stateData: MainViewModel.ViewState) {
        when(stateData) {
            is MainViewModel.ViewState.DoneLoading -> {

            }
            is MainViewModel.ViewState.Loading -> {

            }
        }
    }
    private fun bindViewEvent(stateData: MainViewModel.ViewEvent) {
        when(stateData) {
            is MainViewModel.ViewEvent.RequestPermission -> {
                if(shouldShowGalleryPermissionRationale()==false){
                    requestGalleryPermission(){isPermissionGranted ->
                        if(isPermissionGranted){
                            mainViewModel.loadImage(this)
                        }
                        else {
                            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }
                else{
                    Toast.makeText(this, "You reject permission to many times, please go to setting to re-enable", Toast.LENGTH_SHORT).show()
                }
            }
            is MainViewModel.ViewEvent.ShowImage -> {

            }
            is MainViewModel.ViewEvent.GetImage -> {

            }
        }
    }



}