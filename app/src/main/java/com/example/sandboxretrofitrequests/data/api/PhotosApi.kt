package com.example.sandboxretrofitrequests.data.api

import com.example.sandboxretrofitrequests.data.SearchPhotoData
import com.example.sandboxretrofitrequests.data.PhotoData
import retrofit2.Response
import retrofit2.http.*

interface PhotosApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "-euS_1Y7iW332nMKUbJ7D_xzhM1LuvHOYH_xtpdKt3Y"
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/photos")
    suspend fun initialPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Response<List<PhotoData>>

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Response<SearchPhotoData?>
}