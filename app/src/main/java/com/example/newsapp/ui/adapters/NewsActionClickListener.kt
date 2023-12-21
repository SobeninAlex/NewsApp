package com.example.newsapp.ui.adapters

import com.example.newsapp.models.Article

interface NewsActionClickListener {
    fun onFavoriteClick(article: Article)
    fun onArticleClick(article: Article)

}