package com.fossil.technical.gallery.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.fossil.technical.gallery.R
import com.fossil.technical.gallery.databinding.FragmentMediaViewerBinding
import com.fossil.technical.gallery.extention.gone
import com.fossil.technical.gallery.extention.loadImageByURI
import com.fossil.technical.gallery.extention.parcelable
import com.fossil.technical.gallery.extention.visible
import com.fossil.technical.gallery.model.MediaFile
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.IMAGE_TYPE
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.VIDEO_TYPE
import kotlinx.coroutines.delay
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mediaFile = it.parcelable<MediaFile>(MEDIA_FILE_KEY)

        }

    }

    override fun getLayoutId(): Int =R.layout.fragment_media_viewer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadMediaFile()
    }

    private fun initView() {
        if(mediaFile?.mediaType == VIDEO_TYPE){
            mediaController = MediaController(requireContext())
            mediaController?.setAnchorView(dataBinding.videoView)
            dataBinding.videoView.setMediaController(mediaController)
        }

    }

    private fun loadMediaFile() {
        mediaFile?.let { file ->
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