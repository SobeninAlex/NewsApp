package com.example.newsapp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.FragmentFavoriteBinding
import com.example.newsapp.ui.adapters.ArticleAdapter
import com.example.newsapp.ui.adapters.LoadStateAdapter
import com.example.newsapp.ui.adapters.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteViewModel>()
    private lateinit var articleAdapter: ArticleAdapter

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
        articleAdapter = ArticleAdapter { article ->
            val action = FavoriteFragmentDirections
                .actionFavoriteFragmentToDetailsFragment(article)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = articleAdapter
    }

    private fun viewModelObserver() {
        viewModel.allFavoriteArticles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.differ.submitList(articles)
        }
    }

}