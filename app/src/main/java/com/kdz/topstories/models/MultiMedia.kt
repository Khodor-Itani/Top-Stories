package com.kdz.topstories.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MultiMedia(
    @SerializedName("format")
    val format: String,

    @SerializedName("url")
    val url: String
) : Parcelable