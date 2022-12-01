package com.example.sandboxretrofitrequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UnsplashPhotosViewModel(private val useCase: UseCase) : ViewModel() {

    sealed class ScreenState {
        object Loading : ScreenState()
        class Success(val success: MutableList<PhotoData>) : ScreenState()
        class Error(val error: String) : ScreenState()
    }

    private val _stateFlow = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    fun fetchPhotos(page: Int) {
        viewModelScope.launch {
            viewModelScope.launch {
                useCase(page, object : ServiceInterface.LoadPhotosCallback {
                    override fun onSuccess(success: MutableList<PhotoData>) {
                        _stateFlow.value = ScreenState.Success(success)
                    }

                    override fun onError(error: String) {
                        _stateFlow.value = ScreenState.Error(error)
                    }
                })


            }
        }
    }
}