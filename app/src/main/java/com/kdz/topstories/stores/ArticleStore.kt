package com.kdz.topstories.stores

import com.kdz.topstories.models.Article
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface ArticleStore {
    fun getArticles(section: Section): Maybe<List<ArticleEntity>?>

    fun setArticles(articles: List<ArticleEntity>)

    fun getBookmarkedArticles(): Maybe<List<ArticleEntity>?>

    fun addArticle(article: ArticleEntity)

    fun refreshArticleSection(articles: List<ArticleEntity>, section: Section)
}