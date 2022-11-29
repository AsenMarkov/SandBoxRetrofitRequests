package com.example.sandboxretrofitrequests

interface ServiceInterface {
    fun loadPageList(page: Int, loadPhotosCallback: LoadPhotosCallback)

    interface LoadPhotosCallback{
        fun onSuccess(success: MutableList<PhotoData>)
        fun onError(error: String)
    }
}