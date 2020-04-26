package com.kdz.topstories.ui.fragments.articledetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kdz.topstories.databinding.ArticleDetailsFragmentBinding

/**
 * Displays details for an [com.kdz.topstories.models.ArticleEntity] in a ScrollView.
 */
class ArticleDetailsFragment : Fragment() {

    lateinit var binding: ArticleDetailsFragmentBinding
    val args: ArticleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ArticleDetailsFragmentBinding.inflate(inflater, container, false)

        binding.article = args.article

        return binding.root
    }
}