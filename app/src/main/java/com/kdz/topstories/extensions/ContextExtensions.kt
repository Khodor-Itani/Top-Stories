package com.kdz.topstories.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Sends an intent to view a web page.
 */

fun Context.goToBrowser(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
}