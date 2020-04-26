package com.kdz.topstories.di.modules

import com.kdz.topstories.stores.ArticleStore
import com.kdz.topstories.stores.ArticleStoreImpl
import org.koin.dsl.module

val storesModule = module {
    single { ArticleStoreImpl() as ArticleStore }
}