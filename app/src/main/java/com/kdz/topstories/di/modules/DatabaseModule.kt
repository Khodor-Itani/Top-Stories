package com.kdz.topstories.di.modules

import androidx.room.Room
import com.kdz.topstories.databases.ArticleDatabase
import org.koin.dsl.module

val ARTICLE_DB_NAME = ArticleDatabase::class.java.simpleName

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), ArticleDatabase::class.java, ARTICLE_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
