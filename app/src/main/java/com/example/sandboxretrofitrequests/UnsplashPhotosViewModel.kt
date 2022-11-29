package com.example.sandboxretrofitrequests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UnsplashPhotosViewModel(private val useCase: UseCase): ViewModel() {

    sealed class ScreenState{
        object Loading : ScreenState()
        class Success(val success: List<PhotoData>): ScreenState()
        class Error(val error: Error): ScreenState()
    }

    private val _stateFlow = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val stateFlow = _stateFlow.asStateFlow()

    fun fetchPhotos(photo: List<PhotoData>){
        viewModelScope.launch {
            viewModelScope.launch {
//                _stateFlow.value = ScreenState.Success(useCase())
            }
        }
    }
}