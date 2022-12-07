package com.example.sandboxretrofitrequests

interface PhotosRepository {

    suspend fun getInitialPhotos(page: Int): Result<List<PhotoData>>
    suspend fun getSearchedPhotos(query: String, page: Int): Result<SearchPhotoData?>
}