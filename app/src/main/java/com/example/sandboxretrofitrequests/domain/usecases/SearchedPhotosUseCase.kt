package com.example.sandboxretrofitrequests.domain.usecases

import com.example.sandboxretrofitrequests.domain.unsplash.PhotosRepository

class SearchedPhotosUseCase(private val repository: PhotosRepository) {
    suspend operator fun invoke(query: String, page: Int) = repository.getSearchedPhotos(query, page)
}