package com.example.sandboxretrofitrequests.di


import com.example.sandboxretrofitrequests.domain.usecases.DefaultPhotosUseCase
import com.example.sandboxretrofitrequests.data.api.PhotosApi
import com.example.sandboxretrofitrequests.domain.unsplash.PhotosRepository
import com.example.sandboxretrofitrequests.domain.unsplash.PhotosRepositoryImpl
import com.example.sandboxretrofitrequests.domain.usecases.SearchedPhotosUseCase
import com.example.sandboxretrofitrequests.ui.gallery.UnsplashPhotosViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val koinModule = module {

    single {
        Retrofit.Builder().baseUrl(PhotosApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory<PhotosApi> { get<Retrofit>().create(PhotosApi::class.java) }

    single<PhotosRepository> { PhotosRepositoryImpl(get()) }

    factory { SearchedPhotosUseCase(repository = get()) }
    factory { DefaultPhotosUseCase(repository = get()) }


    viewModel {
        UnsplashPhotosViewModel(
            getSearchedPhotosUseCase = get(),
            getDefaultPhotosUseCase = get()
        )
    }
}
