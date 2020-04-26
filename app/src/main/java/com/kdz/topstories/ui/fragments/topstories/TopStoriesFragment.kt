package com.kdz.topstories.ui.fragments.topstories

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kdz.topstories.databinding.StoryListCellBinding
import com.kdz.topstories.databinding.TopStoriesFragmentBinding
import com.kdz.topstories.models.ArticleEntity
import com.kdz.topstories.ui.ArticleSelectionHandler
import com.kdz.topstories.ui.diffcallbacks.ArticleDiffCallback
import com.kdz.topstories.ui.fragments.sectioncontainer.SectionContainerFragmentDirections
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Displays a list of [ArticleEntity]s in a List.
 * User can bookmark an [ArticleEntity], or click on it to go to [com.kdz.topstories.ui.activities.articledetails.ArticleDetailsActivity].
 */
class TopStoriesFragment : Fragment(), ArticleSelectionHandler {

    val viewModel: TopStoriesViewModel by viewModel()

    lateinit var binding: TopStoriesFragmentBinding
    lateinit var adapter: StoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopStoriesFragmentBinding.inflate(inflater, container, false)

        initAdapter()
        initRecyclerView()
        observeArticles()
        observeSnackbarMessages()

        return binding.root
    }

    private fun observeArticles() {
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            onArticlesReceived(it)
        })
    }

    private fun onArticlesReceived(newList: List<ArticleEntity>) {
        viewLifecycleOwner.lifecycleScope.launch {
            val oldList = adapter.items
            val diffCallback = ArticleDiffCallback(oldList, newList)
            val result = DiffUtil.calculateDiff(diffCallback)
            adapter.items = newList
            result.dispatchUpdatesTo(adapter)
        }
    }

    private fun observeSnackbarMessages() {
        viewModel.snackbar.observe(viewLifecycleOwner, Observer {
            val snackbar = Snackbar.make(
                activity?.findViewById(android.R.id.content)!!,
                context?.getString(it)!!,
                Snackbar.LENGTH_LONG
            )

            snackbar.setAction(android.R.string.ok) {
                snackbar.dismiss()
            }

            snackbar.show()
        })
    }

    private fun initAdapter() {
        adapter =
            StoriesAdapter(
                viewLifecycleOwner,
                viewModel,
                this
            )
    }

    private fun initRecyclerView() {
        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@TopStoriesFragment.adapter
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

class StoriesViewHolder(val binding: StoryListCellBinding) : RecyclerView.ViewHolder(binding.root)

class StoriesAdapter(
    val viewLifecycleOwner: LifecycleOwner,
    val viewModel: TopStoriesViewModel,
    val selectionHandler: ArticleSelectionHandler
) :
    RecyclerView.Adapter<StoriesViewHolder>() {

    var items = listOf<ArticleEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val binding =
            StoryListCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StoriesViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val article = items[position]

        holder.binding.article = article
        holder.binding.viewModel = viewModel
        holder.binding.selectionHandler = selectionHandler
        holder.binding.lifecycleOwner = viewLifecycleOwner
    }
}