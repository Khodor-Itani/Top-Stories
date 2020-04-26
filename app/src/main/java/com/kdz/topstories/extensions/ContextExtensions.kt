package com.kdz.topstories.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.ui.activities.articledetails.ArticleDetailsActivity

/**
 * Helper methods for navigation. Ideally, we'd use Navigation components with single Activity
 * architecture instead.
 */

fun Context.goToBrowser(url: String){
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
}

const val ARTICLE_PARAM = "ARTICLE_PARAM"

fun Context.goToArticleDetails(article: ArticleEntity) {
    val intent = Intent(this, ArticleDetailsActivity::class.java)
    intent.putExtra(ARTICLE_PARAM, article)
    startActivity(intent)
}