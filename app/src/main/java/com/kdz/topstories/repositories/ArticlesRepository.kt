package com.kdz.topstories.repositories

import com.kdz.topstories.models.Article
import com.kdz.topstories.models.Section
import io.reactivex.Observable
import io.reactivex.Single

interface ArticlesRepository {
    fun getTopStories(section: Section): Observable<List<Article>>
    fun getBookmarkedArticles(): Observable<List<Article>>
    fun pollTopStories()
}