package com.example.sandboxretrofitrequests.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoData(
    val id: String,
    val description: String?,
    val urls: UnsplashedUrls,
    val user: User,
    var selected: Boolean = false
) : Parcelable {
    @Parcelize
    data class UnsplashedUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

    @Parcelize
    data class User(
        val id: String,
        val username: String,
        val name: String
    ) : Parcelable
}