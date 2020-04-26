package com.kdz.topstories.ui.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.kdz.topstories.models.ArticleEntity

/**
 * Diffs a list of [ArticleEntity]s. [ArticleEntity] contents are considered different if their
 * [ArticleEntity.isBookmarked] is different, for UI purposes.
 */

class ArticleDiffCallback(val oldList: List<ArticleEntity>, val newList: List<ArticleEntity>) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title.trim().equals(newList[newItemPosition].title.trim())
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isBookmarked == newList[newItemPosition].isBookmarked
    }
}
