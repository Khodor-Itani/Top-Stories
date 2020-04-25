package com.kdz.topstories.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.kdz.topstories.stores.ArticleStore
import com.kdz.topstories.stores.ArticleStoreImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ARTICLE_STORAGE_KEY = "ARTICLE_STORAGE_KEY"

val storesModule = module {
    single { ArticleStoreImpl(get(named(ARTICLE_STORAGE_KEY))) as ArticleStore }

    single(named(ARTICLE_STORAGE_KEY)) {
        provideSharedPreferences(
            get(),
            ARTICLE_STORAGE_KEY
        )
    }
}

fun provideSharedPreferences(context: Context, name: String): SharedPreferences {
    return context.getSharedPreferences(name, Context.MODE_PRIVATE)
}
