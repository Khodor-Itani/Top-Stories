package com.kdz.topstories.stores

import com.kdz.topstories.models.Article
import com.kdz.topstories.models.Section
import io.reactivex.Observable
import io.reactivex.Single

interface ArticleStore {
    fun getArticles(section: Section): Observable<List<Article>>

    fun setArticles(articles: List<Article>, section: Section)

    fun getBookmarkedArticles(): Observable<List<Article>>

    fun setBookmarkedArticles(articles: List<Article>)
}