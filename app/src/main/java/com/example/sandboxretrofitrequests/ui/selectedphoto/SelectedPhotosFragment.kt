package com.example.sandboxretrofitrequests.ui.selectedphoto

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.sandboxretrofitrequests.data.PhotoData
import com.example.sandboxretrofitrequests.domain.adapters.SelectedPhotoAdapter
import com.example.sandboxretrofitrequests.databinding.FragmentSelectedPhotosBinding

class SelectedPhotosFragment : Fragment() {

    private lateinit var binding: FragmentSelectedPhotosBinding
    private val photoAdapter = SelectedPhotoAdapter()

    private val args: SelectedPhotosFragmentArgs by navArgs()
    private lateinit var photosList: List<PhotoData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSelectedPhotosBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photosList = args.photos.toList()

        binding.apply {
            recyclerViewSelectedPhoto.adapter = photoAdapter
        }

        binding.recyclerViewSelectedPhoto.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = photoAdapter
            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(binding.recyclerViewSelectedPhoto)
        }

        if (photosList.isNotEmpty()) {
            photoAdapter.setNewPhoto(photosList)
            photoAdapter.notifyDataSetChanged()
        }
    }
}