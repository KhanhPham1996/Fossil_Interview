package com.fossil.technical.gallery.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fossil.technical.gallery.R
import com.fossil.technical.gallery.databinding.ItemImageInDeviceBinding
import com.fossil.technical.gallery.extention.gone
import com.fossil.technical.gallery.extention.loadImageByURI
import com.fossil.technical.gallery.extention.visible
import com.fossil.technical.gallery.model.MediaFile
import com.fossil.technical.gallery.model.MediaFile.Companion.MEDIA_TYPE.VIDEO_TYPE

class ImageGridAdapter  : ListAdapter<MediaFile, ImageGridAdapter.ImageGridViewHolder>(ItemImageDiffCallBack()) {


    class ImageGridViewHolder(
        val binding : ItemImageInDeviceBinding
    ) : ViewHolder(binding.root){

        fun bind(dataItem : MediaFile){
            binding.imageInDevice.loadImageByURI(dataItem.imageUri)
            if(dataItem.mediaType == VIDEO_TYPE){
                binding.icPlay.visible()
            }
            else{
                binding.icPlay.gone()
            }
        }


        companion object {
            fun from(
                parent: ViewGroup,
            ): ImageGridViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemImageInDeviceBinding.inflate(layoutInflater, parent, false)
                return ImageGridViewHolder(
                    binding,
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
    class ItemImageDiffCallBack() : DiffUtil.ItemCallback<MediaFile>() {
        override fun areItemsTheSame(oldItem: MediaFile, newItem: MediaFile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MediaFile,
            newItem: MediaFile
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageGridViewHolder {
        return ImageGridViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: ImageGridViewHolder, position: Int) {
        getItem(position)?.let {

            holder.bind(it)
        }
    }
}