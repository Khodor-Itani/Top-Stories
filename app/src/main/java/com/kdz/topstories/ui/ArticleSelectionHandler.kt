package com.kdz.topstories.ui

import com.kdz.topstories.models.ArticleEntity

interface ArticleSelectionHandler {
    fun onArticleSelected(article: ArticleEntity)
}