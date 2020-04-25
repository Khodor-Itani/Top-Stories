package com.kdz.topstories.models

import com.google.gson.annotations.SerializedName

data class Article(

    @SerializedName("title")
    val title: String,

    @SerializedName("published_date")
    val publishedDate: String,

    @SerializedName("abstract")
    val abstract: String,

    @SerializedName("url")
    val url: String,

    val isBookmarked: Boolean = false

    // TODO: add photo URL; reference:  https://developer.nytimes.com/docs/top-stories-product/1/types/Article
)