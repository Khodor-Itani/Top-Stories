package com.kdz.topstories.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("bind:imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        if (!it.isBlank()) {
            Picasso.get().load(url).into(imageView)
        }
    }
}