package com.kdz.topstories.ui.fragments.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kdz.topstories.databinding.BookmarksFragmentBinding
import com.kdz.topstories.databinding.BookmarksListCellBinding
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.ui.ArticleSelectionHandler
import com.kdz.topstories.ui.diffcallbacks.ArticleDiffCallback
import com.kdz.topstories.ui.fragments.sectioncontainer.SectionContainerFragmentDirections
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Display a grid of bookmarked [ArticleEntity]s.
 * User can click an article to see its details
 */
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

    private fun onArticlesReceived(newList: List<ArticleEntity>) {

        if (newList.isNullOrEmpty()) {
            binding.mainRecyclerView.visibility = View.GONE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.mainRecyclerView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val oldList = adapter.items
            val diffCallback = ArticleDiffCallback(oldList, newList)
            val result = DiffUtil.calculateDiff(diffCallback)
            adapter.items = newList
            result.dispatchUpdatesTo(adapter)
        }
    }

    private fun initAdapter() {
        adapter = BookmarksAdapter(
            viewLifecycleOwner,
            viewModel,
            this
        )
    }

    private fun initRecyclerView() {
        binding.mainRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@BookmarksFragment.adapter
        }
    }

    override fun onArticleSelected(article: ArticleEntity) {
        findNavController().navigate(
            SectionContainerFragmentDirections.actionSectionContainerFragmentToArticleDetailsFragment(
                article
            )
        )
    }
}

class BookmarksViewHolder(val binding: BookmarksListCellBinding) :
    RecyclerView.ViewHolder(binding.root)

class BookmarksAdapter(
    val viewLifecycleOwner: LifecycleOwner,
    val viewModel: BookmarksViewModel,
    val selectionHandler: ArticleSelectionHandler
) :
    RecyclerView.Adapter<BookmarksViewHolder>() {

    var items = listOf<ArticleEntity>()

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