package com.fossil.technical.gallery.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.fossil.technical.gallery.R
import com.fossil.technical.gallery.databinding.FragmentListMediaBinding
import com.fossil.technical.gallery.extention.bindTo
import com.fossil.technical.gallery.extention.replaceFragmentWithTransition
import com.fossil.technical.gallery.extention.requestGalleryPermission
import com.fossil.technical.gallery.extention.shouldShowGalleryPermissionRationale
import com.fossil.technical.gallery.view.ImageGridAdapter
import com.fossil.technical.gallery.viewmodel.ListMediaViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MediaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaListFragment : BaseFragment<FragmentListMediaBinding>() {

    private lateinit var imageGridAdapter: ImageGridAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                listMediaViewModel.loadImageFromDevice(requireContext())
            } else {
                listMediaViewModel.permissionDenied()

            }
        }
    private val listMediaViewModel: ListMediaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutId(): Int = R.layout.fragment_list_media


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()

        collectDataFromViewModel()
        requestStorageAndGetImage()
    }

    private fun initListener() {
        imageGridAdapter.setOnItemClickListener {
            replaceFragmentWithTransition(
                containerId = R.id.fragment_container,
                fragment = MediaViewerFragment.newInstance(it),
                tag = MediaViewerFragment.TAG,
                enter = R.anim.default_enter_anim,
                exit = R.anim.default_exit_anim,
                popEnter = R.anim.default_pop_enter_anim,
                popExit = R.anim.default_pop_exit_anim
            )
        }
    }

    private fun initView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1 // each item spans 1 column
            }
        }
        imageGridAdapter = ImageGridAdapter()
        dataBinding.recyclerImage.apply {
            layoutManager = gridLayoutManager
            adapter = imageGridAdapter
        }

    }

    private fun requestStorageAndGetImage() {
        listMediaViewModel.requestStoragePermission()
    }

    private fun collectDataFromViewModel() {
        with(listMediaViewModel) {
            bindTo(this.state, ::bindViewStateChange)
            bindTo(this.event, ::bindViewEvent)
        }


    }

    private fun bindViewStateChange(stateData: ListMediaViewModel.ViewState) {
        when (stateData) {
            is ListMediaViewModel.ViewState.DoneLoading -> {

            }
            is ListMediaViewModel.ViewState.Loading -> {

            }
        }
    }


    private fun bindViewEvent(eventData: ListMediaViewModel.ViewEvent) {
        when (eventData) {
            is ListMediaViewModel.ViewEvent.RequestPermission -> {
                if (!shouldShowGalleryPermissionRationale()) {
                    requestGalleryPermission { isPermissionGranted ->
                        if (isPermissionGranted) {
                            listMediaViewModel.loadImageFromDevice(requireContext())
                        } else {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                            }

                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "You reject permission to many times, please go to setting to re-enable",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            is ListMediaViewModel.ViewEvent.ShowImage -> {
                imageGridAdapter.submitList(eventData.listMediaFile)
            }
            is ListMediaViewModel.ViewEvent.GetImage -> {

            }
        }
    }



    override fun handleOnBackPress( callback: (() -> Unit)?) {
        super.handleOnBackPress(callback)
        requireActivity().finish()
    }

    companion object {
        val TAG: String = Companion::class.java.simpleName

        @JvmStatic
        fun newInstance() = MediaListFragment()
    }
}