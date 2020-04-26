package com.kdz.topstories.databases

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import com.kdz.topstories.models.Article
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.models.Section
import io.reactivex.Maybe
import io.reactivex.Observable
import java.lang.Exception

@Dao
abstract class ArticleDao {
    @Query("SELECT * FROM ArticleEntity WHERE section = :section")
    abstract fun getArticles(section: Section): Maybe<List<ArticleEntity>?>

    @Query("SELECT * FROM ArticleEntity WHERE isBookmarked = 1")
    abstract fun getBookmarked(): Maybe<List<ArticleEntity>?>

    @Query("SELECT * FROM ArticleEntity WHERE section = :section")
    abstract fun observeArticles(section: Section): Observable<List<ArticleEntity>?>

    @Query("SELECT * FROM ArticleEntity WHERE isBookmarked = 1")
    abstract fun observeBookmarked(): Observable<List<ArticleEntity>?>

    @Query("DELETE FROM ArticleEntity WHERE isBookmarked = 0 AND section = :section")
    abstract fun deletePreservingBookmarked(section: Section)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insert(articleEntity: ArticleEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(articles: List<ArticleEntity>): List<Long>

    @Update
    abstract fun update(article: ArticleEntity)

    @Update
    abstract fun update(articles: List<ArticleEntity>)

    @Delete
    abstract fun delete(article: ArticleEntity)

    @Transaction
    open fun upsert(article: ArticleEntity) {
        try {
            insert(article)
        } catch (e: SQLiteConstraintException) {
            update(article)
        }
    }

    @Transaction
    open fun upsert(articles: List<ArticleEntity>) {
        val insertResult = insert(articles)
        val updateList = mutableListOf<ArticleEntity>()

        for (i in insertResult.indices) {
            if (insertResult.get(i) == -1L) {
                updateList.add(articles[i])
            }
        }

        if (!updateList.isEmpty()) {
            update(updateList)
        }
    }
}