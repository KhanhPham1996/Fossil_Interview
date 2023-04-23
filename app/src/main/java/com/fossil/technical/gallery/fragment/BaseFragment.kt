package com.fossil.technical.gallery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : ViewDataBinding> : Fragment() {
    // Data Binding
    lateinit var dataBinding: V

    //private var onRequestBLEPermissionResult: ((Boolean) -> Unit)? = null


    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        this.dataBinding.lifecycleOwner = this.viewLifecycleOwner
        return dataBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding.unbind()
        dataBinding.lifecycleOwner = null
    }

    internal open fun handleOnBackPress(callback: (() -> Unit)?) {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    callback?.invoke()

                }
            }
            )
    }
}