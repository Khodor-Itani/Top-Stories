package com.kdz.topstories.api.responses

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

    @SerializedName("status")
    val status: String? = null

    @SerializedName("copyright")
    val copyright: String? = null

    @SerializedName("section")
    val section: String? = null

    @SerializedName("last_updated")
    val lastUpdated: String? = null

    @SerializedName("num_results")
    val numResults: Int? = null

    @SerializedName("results")
    val results: T? = null

    fun isSuccess() = status == "OK"
}