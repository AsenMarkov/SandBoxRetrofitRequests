package com.example.sandboxretrofitrequests

class SearchedPhotosUseCase(private val repository: PhotosRepository) {
    suspend operator fun invoke(query: String, page: Int) = repository.getSearchedPhotos(query, page)
}