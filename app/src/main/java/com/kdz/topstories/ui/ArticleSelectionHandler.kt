package com.kdz.topstories.ui

import com.kdz.topstories.models.ArticleEntity

/**
 * An Interface for Views that support selection of an [ArticleEntity].
 */
interface ArticleSelectionHandler {
    /**
     * Called when an [ArticleEntity] is clicked in order to go to [com.kdz.topstories.ui.activities.articledetails.ArticleDetailsActivity].
     * @param article: The clicked article.
     */
    fun onArticleSelected(article: ArticleEntity)
}