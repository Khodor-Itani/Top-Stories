package com.kdz.topstories.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

/**
 * Load an image URL directly into an ImageView
 */
@BindingAdapter("bind:imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        if (!it.isBlank()) {
            Picasso.get().load(url).into(imageView)
        }
    }
}

private const val SRC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX"
private const val TARGET_DATE_FORMAT = "EEEE, MMMM dd, YYYY - hh:mm a"

private val srcDateFormat = SimpleDateFormat(SRC_DATE_FORMAT, Locale.getDefault())
private val targetDateFormat = SimpleDateFormat(TARGET_DATE_FORMAT, Locale.getDefault())

/**
 * Parses dates and formats dates to set texts directly.
 */
@BindingAdapter("bind:date")
fun setDate(textView: TextView, dateString: String?) {
    val dateString = dateString ?: return

    val date = srcDateFormat.parse(dateString)

    textView.setText(targetDateFormat.format(date))
}

/**
 * Causes a view to redirect to a URL when clicked.
 */
@BindingAdapter("bind:destUrl")
fun goToBrowser(view: View, url: String) {
    view.setOnClickListener {
        view.context.goToBrowser(url)
    }
}