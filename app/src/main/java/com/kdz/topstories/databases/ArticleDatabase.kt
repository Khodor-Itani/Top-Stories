package com.kdz.topstories.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kdz.topstories.databases.typeconverters.RoomTypeConverters
import com.kdz.topstories.models.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}