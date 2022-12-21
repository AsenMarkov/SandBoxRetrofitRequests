package com.example.sandboxretrofitrequests.domain.usecases

import com.example.sandboxretrofitrequests.domain.unsplash.PhotosRepository

class DefaultPhotosUseCase(private val repository: PhotosRepository) {
    suspend operator fun invoke(page: Int) = repository.getInitialPhotos(page)
}