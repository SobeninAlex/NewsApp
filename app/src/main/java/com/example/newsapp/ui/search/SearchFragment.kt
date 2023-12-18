package com.example.newsapp.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.adapters.ArticleAdapter
import com.example.newsapp.ui.adapters.NewsAdapter
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
    private lateinit var newsAdapter: ArticleAdapter

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
        newsAdapter = ArticleAdapter { article ->
            val action = SearchFragmentDirections
                .actionSearchFragmentToDetailsFragment(article)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = newsAdapter
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
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is NetworkResult.Error -> {
                    response.data?.let {
                        Log.d(TAG, "SearchFragment error: $it")
                    }
                }
            }
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