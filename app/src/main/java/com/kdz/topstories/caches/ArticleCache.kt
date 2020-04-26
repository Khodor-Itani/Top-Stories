package com.kdz.topstories.caches

import com.kdz.topstories.models.Article
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Maybe
import io.reactivex.Observable

interface ArticleCache {
    fun getObservableArticles(section: Section): Observable<List<ArticleEntity>?>

    fun setArticles(articles: List<ArticleEntity>, section: Section)

    fun getArticles(section: Section): Maybe<List<ArticleEntity>?>

    fun getObservableBookmarkedArticles(): Observable<List<ArticleEntity>?>

    fun getBookmarkedArticles(): Maybe<List<ArticleEntity>?>

    fun setBookmarkedArticles(articles: List<ArticleEntity>)
}