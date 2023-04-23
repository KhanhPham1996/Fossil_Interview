package com.fossil.technical.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.fossil.technical.gallery.databinding.ActivityMainBinding
import com.fossil.technical.gallery.extention.*
import com.fossil.technical.gallery.fragment.MediaListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        if (savedInstanceState == null) {
            replaceFragmentWithNoTransition(
                R.id.fragment_container,
                MediaListFragment.newInstance(),
                MediaListFragment.TAG
            )
        }

    }





}