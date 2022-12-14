package com.example.sandboxretrofitrequests.data

import com.example.sandboxretrofitrequests.data.PhotoData
import com.google.gson.annotations.SerializedName

data class SearchPhotoData(
    @SerializedName("total")
    val total: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("results")
    val result: List<PhotoData>?
)
