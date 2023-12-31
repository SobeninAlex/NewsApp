package com.example.newsapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoriteBinding
import com.example.newsapp.models.Article
import com.example.newsapp.ui.adapters.ArticleAdapter
import com.example.newsapp.ui.adapters.NewsActionClickListener
import com.example.newsapp.ui.adapters.OnImageListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteViewModel>()

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: List<Article>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModelObserver()
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
                val action = FavoriteFragmentDirections
                    .actionFavoriteFragmentToDetailsFragment(article)
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
        viewModel.allFavoriteArticles.observe(viewLifecycleOwner) { list ->
            articleAdapter.differ.submitList(list)
            articleList = list
        }
    }

}