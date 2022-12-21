package com.example.sandboxretrofitrequests.domain.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.sandboxretrofitrequests.data.PhotoData
import com.example.sandboxretrofitrequests.databinding.UnsplashPhotoItemBinding

@SuppressLint("NotifyDataSetChanged")
class PhotoAdapter(
    private val listener: OnPhotoClickedListener, private val showDeleteMenu: (Boolean) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private lateinit var binding: UnsplashPhotoItemBinding

    private var photoDataListFiltered: MutableList<PhotoData>? = null
    private var photoDataList: MutableList<PhotoData>? = null

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

        val currentPhoto = photoDataListFiltered?.get(position)
        holder.backGroundSelected.visibility = View.GONE

        holder.bind(currentPhoto!!)
    }

    inner class PhotoViewHolder(private val binding: UnsplashPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val backGroundSelected: ImageView = binding.selectedBackground

        init {
            binding.imageView.setOnClickListener {
                val item = photoDataListFiltered?.get(adapterPosition)

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
                photoDataListFiltered?.get(adapterPosition)?.let {
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
            backGroundSelected.visibility =
                if (photo.selected) View.VISIBLE else View.GONE // checks if the image is selected or not when recreating it while scrolling
            binding.apply {
                Glide.with(imageView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
                binding.userTextView.text = photo.user.name
            }
        }
    }

    fun setNewPhoto(
        updatedPhotosList: MutableList<PhotoData>,
        oldCount: Int,
        photoDataListSize: Int
    ) {
        photoDataList = updatedPhotosList
        photoDataListFiltered = photoDataList
        notifyDataSetChanged()
        notifyItemRangeChanged(oldCount, photoDataListSize)
    }

    fun clearAllPhotos() {
        photoDataListFiltered?.clear()
        listOfSelectedImages.clear()
        isEnable = false
        notifyDataSetChanged()
    }

    fun clearSelectedPhotos() {
        photoDataListFiltered?.forEach {
            it.selected = false
        }
        listOfSelectedImages.clear()
        isEnable = false
        notifyDataSetChanged()
    }

    fun deleteSelected() {
        val newPhotoList = photoDataListFiltered
        newPhotoList?.removeAll { item -> item.selected }
        photoDataListFiltered = newPhotoList
        isEnable = false
        listOfSelectedImages.clear()
        notifyDataSetChanged()
    }

    fun getClickedPhoto(): List<PhotoData> {
        return listOfSelectedImages
    }

    override fun getItemCount(): Int = photoDataListFiltered?.size ?: 0


    interface OnPhotoClickedListener {
        fun onClickedPhoto()
        fun editorModeOn(enabled: Boolean)
    }
}

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                photoDataListFiltered = if (constraint.toString().isEmpty()) {
//                    photoDataList
//                } else {
//                    val filteredList = mutableListOf<PhotoData>()
//
//                    photoDataList?.filter {
//                        it.urls.regular.contains(constraint!!)
//                    }?.forEach { filteredList.add(it) }
//                    filteredList
//                }
//                return FilterResults().apply { values = photoDataListFiltered }
//            }

//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                photoDataListFiltered =
//                    if (results?.values == null) mutableListOf() else results.values as MutableList<PhotoData>
//                notifyDataSetChanged()
//            }
