package com.kdz.topstories.datasources

import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Single

interface ArticleDataSource {
    fun getArticles(section: Section): Single<List<ArticleEntity>?>
}