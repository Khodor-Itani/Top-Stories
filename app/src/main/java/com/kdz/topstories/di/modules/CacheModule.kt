package com.kdz.topstories.di.modules

import com.kdz.topstories.caches.ArticleCache
import com.kdz.topstories.caches.ArticleCacheImpl
import org.koin.dsl.module

val cacheModule = module {
    single { ArticleCacheImpl() as ArticleCache }
}