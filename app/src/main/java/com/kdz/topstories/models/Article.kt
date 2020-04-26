package com.kdz.topstories.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(

    @SerializedName("title")
    val title: String,

    @SerializedName("published_date")
    val publishedDate: String,

    @SerializedName("abstract")
    val abstract: String,

    @SerializedName("url")
    val url: String?,

    @SerializedName("multimedia")
    val multiMedia: List<MultiMedia>,

    var isBookmarked: Boolean = false

) : Parcelable {

    fun getThumbnail(): String? {
        return multiMedia.find {
            it.format.equals("Standard Thumbnail", true)
        }?.url ?: ""
    }

    fun getLargeImage(): String? {
        return multiMedia.find {
            it.format.equals("superJumbo", true)
        }?.url ?: ""
    }
}