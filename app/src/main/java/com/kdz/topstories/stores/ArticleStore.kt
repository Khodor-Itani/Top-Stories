package com.kdz.topstories.stores

import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Maybe

interface ArticleStore {
    fun getArticles(section: Section): Maybe<List<ArticleEntity>?>

    fun setArticles(articles: List<ArticleEntity>)

    fun getBookmarkedArticles(): Maybe<List<ArticleEntity>?>

    fun addArticle(article: ArticleEntity)

    fun refreshArticleSection(articles: List<ArticleEntity>, section: Section)
}