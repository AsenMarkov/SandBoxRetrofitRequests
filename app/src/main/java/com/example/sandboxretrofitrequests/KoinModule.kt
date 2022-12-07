package com.example.sandboxretrofitrequests


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
