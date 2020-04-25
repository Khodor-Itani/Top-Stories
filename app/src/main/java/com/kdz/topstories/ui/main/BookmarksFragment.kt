package com.kdz.topstories.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kdz.topstories.databinding.BookmarksFragmentBinding
import com.kdz.topstories.databinding.BookmarksListCellBinding
import com.kdz.topstories.extensions.goToArticleDetails
import com.kdz.topstories.models.Article
import com.kdz.topstories.ui.main.diffcallbacks.ArticleDiffCallback
import kotlinx.coroutines.selects.select
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarksFragment : Fragment(), ArticleSelectionHandler {

    val viewModel: BookmarksViewModel by viewModel()
    lateinit var binding: BookmarksFragmentBinding

    lateinit var adapter: BookmarksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BookmarksFragmentBinding.inflate(inflater, container, false)

        initAdapter()
        initRecyclerView()
        observeArticles()
        return binding.root
    }

    private fun observeArticles() {
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            onArticlesReceived(it)
        })
    }

    private fun onArticlesReceived(newList: List<Article>) {
        val oldList = adapter.items
        val diffCallback = ArticleDiffCallback(oldList, newList)
        val result = DiffUtil.calculateDiff(diffCallback)
        adapter.items = newList
        result.dispatchUpdatesTo(adapter)
    }

    private fun initAdapter() {
        adapter = BookmarksAdapter(viewLifecycleOwner, viewModel, this)
    }

    private fun initRecyclerView() {
        binding.mainRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@BookmarksFragment.adapter
        }
    }

    override fun onArticleSelected(article: Article) {
        activity?.goToArticleDetails(article)
    }
}

class BookmarksViewHolder(val binding: BookmarksListCellBinding) : RecyclerView.ViewHolder(binding.root)

class BookmarksAdapter(val viewLifecycleOwner: LifecycleOwner, val viewModel: BookmarksViewModel, val selectionHandler: ArticleSelectionHandler) :
    RecyclerView.Adapter<BookmarksViewHolder>() {

    var items = listOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val binding =
            BookmarksListCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookmarksViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        val article = items[position]

        holder.binding.article = article
        holder.binding.viewModel = viewModel
        holder.binding.selectionHandler = selectionHandler
        holder.binding.lifecycleOwner = viewLifecycleOwner
    }

}