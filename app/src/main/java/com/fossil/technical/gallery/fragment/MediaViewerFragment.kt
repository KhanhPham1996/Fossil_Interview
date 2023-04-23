package com.fossil.technical.gallery.fragment


import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fossil.technical.gallery.R
import com.fossil.technical.gallery.databinding.FragmentMediaViewerBinding
import com.fossil.technical.gallery.extention.*
import com.fossil.technical.gallery.model.MediaFile
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.IMAGE_TYPE
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.VIDEO_TYPE
import com.fossil.technical.gallery.viewmodel.ListMediaViewModel
import com.fossil.technical.gallery.viewmodel.MediaDetailViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MEDIA_FILE_KEY = "mediaFileKey"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MediaViewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaViewerFragment : BaseFragment<FragmentMediaViewerBinding>() {

    private var mediaController: MediaController? = null
    private var mediaFile: MediaFile? = null
    private val mediaDetailViewModel: MediaDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mediaFile = it.parcelable<MediaFile>(MEDIA_FILE_KEY)

        }

    }

    override fun getLayoutId(): Int = R.layout.fragment_media_viewer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadMediaFile()
        collectDataFromViewModel()
    }

    private fun collectDataFromViewModel() {
        with(mediaDetailViewModel) {
            bindTo(this.event, ::bindViewEvent)
        }


    }

    private fun bindViewEvent(eventData: MediaDetailViewModel.ViewEvent) {
        when (eventData) {
            is MediaDetailViewModel.ViewEvent.ShowMediaFile -> {
                eventData.mediaFile?.let { file ->
                    when (file?.mediaType) {
                        IMAGE_TYPE -> {
                            dataBinding.videoView.gone()
                            dataBinding.imageView.visible()
                            mediaFile?.let {
                                dataBinding.imageView.loadImageByURI(it.fileURI)

                            }
                        }
                        VIDEO_TYPE -> {
                            dataBinding.videoView.visible()
                            dataBinding.imageView.gone()
                            dataBinding.videoView.setVideoURI(file.fileURI)
                            dataBinding.videoView.requestFocus()
                            dataBinding.videoView.start()

                        }
                        else -> {

                        }

                    }
                }

            }
            is MediaDetailViewModel.ViewEvent.SaveOrDeleteFavorite -> {

            }
        }
    }


    private fun initView() {
        if (mediaFile?.mediaType == VIDEO_TYPE) {
            mediaController = MediaController(requireContext())
            mediaController?.setAnchorView(dataBinding.videoView)
            dataBinding.videoView.setMediaController(mediaController)
        }

    }

    private fun loadMediaFile() {
        mediaFile?.let { mediaDetailViewModel.showMediaToUI(it) }


    }

    override fun handleOnBackPress(callback: (() -> Unit)?) {
        super.handleOnBackPress(callback)
        if (mediaController != null) {
            mediaController!!.hide();
        }

        if (dataBinding.videoView.isPlaying()) {
            dataBinding.videoView.stopPlayback();
            dataBinding.videoView.suspend();
        }
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {

        val TAG: String = Companion::class.java.simpleName

        @JvmStatic
        fun newInstance(mediaFile: MediaFile) =
            MediaViewerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDIA_FILE_KEY, mediaFile)
                }
            }
    }
}