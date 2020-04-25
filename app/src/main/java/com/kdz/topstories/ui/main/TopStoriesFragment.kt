package com.kdz.topstories.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kdz.topstories.databinding.StoryListCellBinding
import com.kdz.topstories.databinding.TopStoriesFragmentBinding
import com.kdz.topstories.models.Article
import com.kdz.topstories.ui.main.diffcallbacks.ArticleDiffCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopStoriesFragment : Fragment() {

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
        adapter = StoriesAdapter(viewLifecycleOwner, viewModel)
    }

    private fun initRecyclerView() {
        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = this@TopStoriesFragment.adapter
        }
    }

}

class StoriesViewHolder(val binding: StoryListCellBinding) : RecyclerView.ViewHolder(binding.root)

class StoriesAdapter(val viewLifecycleOwner: LifecycleOwner, val viewModel: TopStoriesViewModel) :
    RecyclerView.Adapter<StoriesViewHolder>() {

    var items = listOf<Article>()

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
        holder.binding.lifecycleOwner = viewLifecycleOwner
    }

}