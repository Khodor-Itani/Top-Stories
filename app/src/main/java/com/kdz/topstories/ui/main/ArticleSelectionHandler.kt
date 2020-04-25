package com.kdz.topstories.ui.main

import com.kdz.topstories.models.Article

interface ArticleSelectionHandler {
    fun onArticleSelected(article: Article)
}