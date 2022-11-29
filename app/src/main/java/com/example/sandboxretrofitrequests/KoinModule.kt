package com.example.sandboxretrofitrequests


import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {

    factory<ServiceInterface> { ServiceImplementation() }
    factory { UseCase(service = get()) }
    viewModel { UnsplashPhotosViewModel(useCase = get()) }
}
