package com.example.sandboxretrofitrequests

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sandboxretrofitrequests.databinding.UnsplashPhotoItemBinding

class PhotoAdapter(
    private val listener: OnPhotoClickedListener, private val showDeleteMenu: (Boolean) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private lateinit var binding: UnsplashPhotoItemBinding

    private var photoDataList: List<PhotoData>? = null

    private var isEnable = false
    private var listOfSelectedImages = mutableListOf<PhotoData>()
    private var itemsCount: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        binding = UnsplashPhotoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PhotoViewHolder(binding)

    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        val currentPhoto = photoDataList?.get(position)
        holder.check.visibility = View.GONE

        holder.bind(currentPhoto!!)
    }

    inner class PhotoViewHolder(private val binding: UnsplashPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val check: ImageView = binding.selectedBackground

        init {
            binding.imageView.setOnClickListener {
                val item = photoDataList?.get(adapterPosition)

                item?.let {
                    if (isEnable && itemsCount > 0) {
                        if (item.selected) {
                            itemsCount--
                        } else {
                            itemsCount++
                        }
                        if (!listOfSelectedImages.contains(item)) {
                            listOfSelectedImages.add(item)
                        } else {
                            listOfSelectedImages.remove(item)
                        }
                        item.selected = !item.selected
                        notifyItemChanged(adapterPosition)
                    } else {
                        listOfSelectedImages.add(item)
                        listener.onClickedPhoto()

                    }
                    listener.editorModeOn(itemsCount > 0)
                }
            }
            binding.imageView.setOnLongClickListener {
                photoDataList?.get(adapterPosition)?.let {
                    isEnable = true
                    if (!listOfSelectedImages.contains(it)) {
                        listOfSelectedImages.add(it)
                    } else {
                        listOfSelectedImages.remove(it)
                    }
                    it.selected = true
                    listener.editorModeOn(true)
                    notifyItemChanged(adapterPosition)
                    itemsCount++
                    showDeleteMenu(true)
                }
                true
            }
        }

        fun bind(photo: PhotoData) {
            check.visibility =
                if (photo.selected) View.VISIBLE else View.GONE // checks if the image is selected or not when recreating it while scrolling
            binding.apply {
                Glide.with(imageView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .into(imageView)
                binding.userTextView.text = photo.user.name
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(photoDataList: List<PhotoData>, oldCount: Int, photoDataListSize: Int) {
        this.photoDataList = photoDataList
        notifyDataSetChanged()
        notifyItemRangeChanged(oldCount, photoDataListSize)
    }

    fun setNewPhoto(photo: List<PhotoData>) {
        photoDataList = photo
    }

    fun deleteSelected() {
        val newPhotoList = photoDataList?.toMutableList()
        newPhotoList?.removeAll { item -> item.selected }
        photoDataList = newPhotoList?.toList()
        isEnable = false
        listOfSelectedImages.clear()
    }

    fun getClickedPhoto(): List<PhotoData> {
        return listOfSelectedImages
    }

    override fun getItemCount(): Int {
        return photoDataList?.size ?: 0
    }

    interface OnPhotoClickedListener {
        fun onClickedPhoto()
        fun editorModeOn(enabled: Boolean)
    }
}