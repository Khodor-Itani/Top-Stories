package com.kdz.topstories.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kdz.topstories.databases.typeconverters.RoomTypeConverters
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class ArticleEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "title")
    var title: String = "",
    @ColumnInfo(name= "published_date")
    var publishedDate: String = "",
    @ColumnInfo(name= "abstract")
    var abstract: String = "",
    @ColumnInfo(name= "url")
    var url: String = "",
    @ColumnInfo(name= "isBookmarked")
    var isBookmarked: Boolean = false,
    @ColumnInfo(name= "thumbnail_url")
    var thumbNailUrl: String = "",
    @ColumnInfo(name= "large_image_url")
    var largeImageUrl: String = "",
    @TypeConverters(RoomTypeConverters::class)
    var section: Section
) : Parcelable {
    constructor() : this("", "", "", "", false, "", "", Section.US)

    companion object {
        @JvmStatic
        fun copyFromArticle(article: Article, section: Section): ArticleEntity {
            return ArticleEntity(
                article.title,
                article.publishedDate,
                article.abstract,
                article.url ?: "",
                article.isBookmarked,
                article.getThumbnail() ?: "",
                article.getLargeImage() ?: "",
                section
            )
        }
    }
}