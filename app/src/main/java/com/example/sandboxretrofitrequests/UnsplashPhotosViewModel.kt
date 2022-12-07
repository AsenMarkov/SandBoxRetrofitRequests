package com.example.sandboxretrofitrequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UnsplashPhotosViewModel(
    private val getDefaultPhotosUseCase: DefaultPhotosUseCase,
    private val getSearchedPhotosUseCase: SearchedPhotosUseCase
) : ViewModel() {

    sealed class ScreenState {
        object Loading : ScreenState()
        class Success(val photos: List<PhotoData>) : ScreenState()
        class Error(val error: String) : ScreenState()
    }

    private val _stateFlow = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    private var searchJob: Job? = null
    private var debouncePeriod: Long = 1000
    private var currentPage = 0

    fun firstPage(){
        currentPage = 0
    }

    fun getInitialPhotos() {
        currentPage++
        viewModelScope.launch {
            getDefaultPhotosUseCase(currentPage).fold(
                onSuccess = {
                    _stateFlow.value = ScreenState.Success(it)
                },
                onFailure = {
                    _stateFlow.value = ScreenState.Error(it.localizedMessage)
                }
            )
        }
    }

    fun getSearchedPhotos(query: String) {
        currentPage++
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            getSearchedPhotosUseCase(query = query, page = currentPage).fold(
                onSuccess = {
                    _stateFlow.value = ScreenState.Success(it?.result ?: emptyList())
                },
                onFailure = {
                    _stateFlow.value = ScreenState.Error(it.localizedMessage)
                }
            )
        }
    }
}