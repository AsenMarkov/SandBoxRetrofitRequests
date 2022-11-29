package com.example.sandboxretrofitrequests

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceImplementation : ServiceInterface {

    private val api = RetrofitInstance().api

    override fun loadPageList(page: Int, loadPhotosCallback: ServiceInterface.LoadPhotosCallback) {

        val callback: Callback<List<PhotoData>> = object :
            Callback<List<PhotoData>> {

            override fun onResponse(
                call: Call<List<PhotoData>>,
                response: Response<List<PhotoData>>
            ) {
                if (response.isSuccessful) {
                    response.body()
                        ?.let { loadPhotosCallback.onSuccess(it as MutableList<PhotoData>) }
                } else {
                    loadPhotosCallback.onError(response.message())
                }
            }

            override fun onFailure(call: Call<List<PhotoData>>, t: Throwable) {
                t.localizedMessage?.let {
                    loadPhotosCallback.onError(it)
                }
            }
        }
        api.getPhotos(page).enqueue(callback)
    }
}