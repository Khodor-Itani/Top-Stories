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
    /**
     * Get all [ArticleEntity]s.
     */
    @Query("SELECT * FROM ArticleEntity WHERE section = :section")
    abstract fun getArticles(section: Section): Maybe<List<ArticleEntity>?>

    /**
     * Get bookmarked [ArticleEntity]s.
     */
    @Query("SELECT * FROM ArticleEntity WHERE isBookmarked = 1")
    abstract fun getBookmarked(): Maybe<List<ArticleEntity>?>

    /**
     * Observe all [ArticleEntity]s for a [Section].
     */
    @Query("SELECT * FROM ArticleEntity WHERE section = :section")
    abstract fun observeArticles(section: Section): Observable<List<ArticleEntity>?>

    /**
     * Observe all bookmarked [ArticleEntity]s.
     */
    @Query("SELECT * FROM ArticleEntity WHERE isBookmarked = 1")
    abstract fun observeBookmarked(): Observable<List<ArticleEntity>?>

    /**
     * Delete all [ArticleEntity]s while retaining bookmarked articles. Useful when refreshing
     * the list on the main page. Also prevents from persisting old and unused articles.
     */
    @Query("DELETE FROM ArticleEntity WHERE isBookmarked = 0 AND section = :section")
    abstract fun deletePreservingBookmarked(section: Section)

    /**
     * Insert a single [ArticleEntity]. Abort the transaction if the article already exists.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insert(articleEntity: ArticleEntity): Long

    /**
     * Insert [ArticleEntity]s, ignoring already existing ones.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(articles: List<ArticleEntity>): List<Long>

    /**
     * Update a single [ArticleEntity].
     */
    @Update
    abstract fun update(article: ArticleEntity)

    /**
     * Update a list of [ArticleEntity]s.
     */
    @Update
    abstract fun update(articles: List<ArticleEntity>)

    /**
     * Delete a single [ArticleEntity].
     */
    @Delete
    abstract fun delete(article: ArticleEntity)

    /**
     * Insert an [ArticleEntity], or update if it already exists.
     */
    @Transaction
    open fun upsert(article: ArticleEntity) {
        try {
            insert(article)
        } catch (e: SQLiteConstraintException) {
            update(article)
        }
    }

    /**
     * Insert a list of [ArticleEntity]s. Updates the articles that already exist.
     */
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