package com.example.sandboxretrofitrequests.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sandboxretrofitrequests.data.PhotoData
import com.example.sandboxretrofitrequests.databinding.FullscreenDisplayPhotoBinding

class SelectedPhotoAdapter :
    RecyclerView.Adapter<SelectedPhotoAdapter.SelectedPhotoViewHolder>() {
    private lateinit var binding: FullscreenDisplayPhotoBinding
    private var photoDataList: List<PhotoData>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPhotoViewHolder {
        binding = FullscreenDisplayPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectedPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedPhotoViewHolder, position: Int) {
        val currentPhoto = photoDataList?.get(position)
        holder.bind(currentPhoto!!)
    }

    inner class SelectedPhotoViewHolder(private val binding: FullscreenDisplayPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoData) {
            Glide.with(binding.fullScreenImage)
                .load(photo.urls.regular)
                .centerCrop()
                .into(binding.fullScreenImage)
            binding.fullScreenUserTextView.text = photo.user.name
        }
    }

    fun setNewPhoto(photo: List<PhotoData>) {
        photoDataList = photo
    }

    override fun getItemCount(): Int {
        return photoDataList?.size ?: 0
    }
}