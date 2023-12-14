package com.example.newsapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.newsapp.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val allArticles = newsRepository.getAllFavouriteArticles().asLiveData()

}