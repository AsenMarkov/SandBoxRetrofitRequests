package com.example.sandboxretrofitrequests.domain.unsplash

import com.example.sandboxretrofitrequests.data.SearchPhotoData
import com.example.sandboxretrofitrequests.data.PhotoData

interface PhotosRepository {

    suspend fun getInitialPhotos(page: Int): Result<List<PhotoData>>
    suspend fun getSearchedPhotos(query: String, page: Int): Result<SearchPhotoData?>
}