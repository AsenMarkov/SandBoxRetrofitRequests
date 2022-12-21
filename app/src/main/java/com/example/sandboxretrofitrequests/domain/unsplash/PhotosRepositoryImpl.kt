package com.example.sandboxretrofitrequests.domain.unsplash

import com.example.sandboxretrofitrequests.data.SearchPhotoData
import com.example.sandboxretrofitrequests.data.PhotoData
import com.example.sandboxretrofitrequests.data.api.PhotosApi

class PhotosRepositoryImpl(
    private val api: PhotosApi
) : PhotosRepository {

    override suspend fun getInitialPhotos(page: Int): Result<List<PhotoData>> =
        try {
            val response = api.initialPhotos(page)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception())
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }

    override suspend fun getSearchedPhotos(query: String, page: Int): Result<SearchPhotoData?> =
        try {
            val response = api.searchPhotos(query, page)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception())
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
}