package com.fossil.technical.gallery

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.fossil.technical.gallery.databinding.ActivityMainBinding
import com.fossil.technical.gallery.extention.bindTo
import com.fossil.technical.gallery.extention.requestGalleryPermission
import com.fossil.technical.gallery.extention.shouldShowGalleryPermissionRationale
import com.fossil.technical.gallery.view.ImageGridAdapter
import com.fossil.technical.gallery.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageGridAdapter: ImageGridAdapter
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                mainViewModel.loadImageFromDevice(this)
            } else {
                mainViewModel.permissionDenied()

            }
        }
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        initView()
        collectDataFromViewModel()
        requestStorageAndGetImage()
    }

    private fun initView() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1 // each item spans 1 column
            }
        }
        imageGridAdapter = ImageGridAdapter()
        binding.recyclerImage.apply {
            layoutManager = gridLayoutManager
            adapter = imageGridAdapter
        }
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
        when (stateData) {
            is MainViewModel.ViewState.DoneLoading -> {

            }
            is MainViewModel.ViewState.Loading -> {

            }
        }
    }


    private fun bindViewEvent(eventData: MainViewModel.ViewEvent) {
        when (eventData) {
            is MainViewModel.ViewEvent.RequestPermission -> {
                if (!shouldShowGalleryPermissionRationale()) {
                    requestGalleryPermission { isPermissionGranted ->
                        if (isPermissionGranted) {
                            mainViewModel.loadImageFromDevice(this)
                        } else {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }
                            else{
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                            }

                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "You reject permission to many times, please go to setting to re-enable",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is MainViewModel.ViewEvent.ShowImage -> {
                imageGridAdapter.submitList(eventData.listMediaFile)
            }
            is MainViewModel.ViewEvent.GetImage -> {

            }
        }
    }


}