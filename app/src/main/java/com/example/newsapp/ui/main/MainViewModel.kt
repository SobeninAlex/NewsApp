package com.example.newsapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.NewsRepository
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _news = MutableLiveData<NetworkResult<NewsResponse>>()
    val news: LiveData<NetworkResult<NewsResponse>> get() = _news

    private var newsPage = 1

    init {
        getNews()
    }

    fun getNews() {
        viewModelScope.launch {
            _news.postValue(NetworkResult.Loading())
            val response = newsRepository.getNews(page = newsPage)
            if (response.isSuccessful) {
                response.body().let {
                    _news.postValue(NetworkResult.Success(data = it))
                }
            } else {
                _news.postValue(NetworkResult.Error(message = response.message()))
            }
        }
//        newsPage++
    }


}