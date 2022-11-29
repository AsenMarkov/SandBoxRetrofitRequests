package com.example.sandboxretrofitrequests

class UseCase(private val service: ServiceInterface) {

    operator fun invoke(page: Int, callback: ServiceInterface.LoadPhotosCallback) = service.loadPageList(page, callback)
}