package com.kdz.topstories.di.modules

import com.kdz.topstories.datasources.ArticleDataSource
import com.kdz.topstories.datasources.ArticleDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single { ArticleDataSourceImpl() as ArticleDataSource }
}