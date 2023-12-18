package com.example.newsapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentMainBinding
import com.example.newsapp.models.Article
import com.example.newsapp.ui.adapters.LoadStateAdapter
import com.example.newsapp.ui.adapters.NewsAdapter
import com.example.newsapp.utils.Constants.Companion.TAG
import com.example.newsapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModelObserver()
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter { article ->
            val action = MainFragmentDirections
                .actionMainFragmentToDetailsFragment(article)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = newsAdapter.withLoadStateFooter(LoadStateAdapter())

        newsAdapter.addLoadStateListener { state ->
            binding.recyclerView.isVisible = state.refresh != LoadState.Loading
            binding.progressBar.isVisible = state.refresh == LoadState.Loading
        }
    }

    private fun viewModelObserver() {

        lifecycleScope.launch {
            viewModel.pagingNews.collectLatest(newsAdapter::submitData)
        }

    }

}