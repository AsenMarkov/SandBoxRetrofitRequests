package com.example.sandboxretrofitrequests

class DefaultPhotosUseCase(private val repository: PhotosRepository) {
    suspend operator fun invoke(page: Int) = repository.getInitialPhotos(page)
}