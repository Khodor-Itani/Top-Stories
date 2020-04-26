package com.kdz.topstories.ui.activities.articledetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kdz.topstories.R
import com.kdz.topstories.databinding.ArticleDetailsActivityBinding
import com.kdz.topstories.extensions.ARTICLE_PARAM

class ArticleDetailsActivity : AppCompatActivity() {

    lateinit var binding: ArticleDetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.article_details_activity)

        binding.article = intent.getParcelableExtra(ARTICLE_PARAM)
        binding.lifecycleOwner = this
    }
}