package com.kdz.topstories.ui.main.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.kdz.topstories.models.Article

class ArticleDiffCallback(val oldList: List<Article>, val newList: List<Article>) :
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
        return oldList[oldItemPosition].isBookmarked.equals(newList[newItemPosition].isBookmarked)
    }
}
