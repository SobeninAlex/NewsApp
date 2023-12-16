package com.example.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.NewsRepository
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _newsSearch = MutableLiveData<NetworkResult<NewsResponse>>()
    val newsSearch: LiveData<NetworkResult<NewsResponse>> get() = _newsSearch

    private var newsPage = 1

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            _newsSearch.postValue(NetworkResult.Loading())
            val response = newsRepository.searchNews(searchQuery, newsPage)
            if (response.isSuccessful) {
                response.body()?.let {
                    _newsSearch.postValue(NetworkResult.Success(data = it))
                }
            } else {
                _newsSearch.postValue(NetworkResult.Error(message = response.message()))
            }
        }
    }

}