package com.example.newsapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.NewsRepository
import com.example.newsapp.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val allFavoriteArticles = newsRepository.getAllFavouriteArticles().asLiveData()

    fun saveArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.saveArticle(article)
        }

    fun deleteArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteArticle(article)
        }

}