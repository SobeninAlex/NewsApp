package com.example.newsapp.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.models.Article
import com.example.newsapp.ui.adapters.ArticleAdapter
import com.example.newsapp.ui.adapters.NewsActionClickListener
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.ui.adapters.OnImageListener
import com.example.newsapp.ui.favorite.FavoriteFragmentDirections
import com.example.newsapp.ui.main.MainFragmentDirections
import com.example.newsapp.utils.Constants.Companion.TAG
import com.example.newsapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchViewModel>()

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: List<Article>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModelObserver()
        searchNews()
    }

    private fun initAdapter() {
        articleAdapter = ArticleAdapter(object : NewsActionClickListener {
            override fun onFavoriteClick(article: Article) {
                if (articleList.contains(article)) {
                    viewModel.deleteArticle(article)
                } else {
                    viewModel.saveArticle(article)
                }
            }

            override fun onArticleClick(article: Article) {
                val action = SearchFragmentDirections
                    .actionSearchFragmentToDetailsFragment(article)
                findNavController().navigate(action)
            }
        })

        binding.recyclerView.adapter = articleAdapter

        articleAdapter.onImageListener = object : OnImageListener {
            override fun setImage(article: Article, imageView: ImageView) {
                if (articleList.contains(article)) {
                    imageView.setImageResource(R.drawable.icon_favorite_added)
                } else {
                    imageView.setImageResource(R.drawable.icon_heart)
                }
            }
        }
    }

    private fun viewModelObserver() {
        viewModel.newsSearch.observe(viewLifecycleOwner) { response ->
            when(response) {
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    response.data?.let {
                        articleAdapter.differ.submitList(it.articles)
                    }
                }
                is NetworkResult.Error -> {
                    response.data?.let {
                        Log.d(TAG, "SearchFragment error: $it")
                    }
                }
            }
        }

        viewModel.allFavoriteArticles.observe(viewLifecycleOwner) {
            articleList = it
        }
    }

    private fun searchNews() {
        var job: Job? = null
        binding.editText.addTextChangedListener { text ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                text?.let {
                    if (it.toString().trim().isNotBlank()) {
                        viewModel.searchNews(searchQuery = it.toString())
                    }
                }
            }
        }
    }

}