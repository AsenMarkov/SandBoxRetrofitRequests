package com.example.sandboxretrofitrequests

import retrofit2.Call
import retrofit2.http.*

interface RetrofitApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "-euS_1Y7iW332nMKUbJ7D_xzhM1LuvHOYH_xtpdKt3Y"
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/photos")
    fun getPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int = 10): Call<List<PhotoData>>
//    suspend fun getPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int = 10): Response<List<PhotoDataJson>>
}