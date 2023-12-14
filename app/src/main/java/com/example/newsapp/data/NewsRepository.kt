package com.example.newsapp.data

import com.example.newsapp.data.api.NewsService
import com.example.newsapp.data.database.ArticleDao
import com.example.newsapp.models.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    private val articleDao: ArticleDao
) {
    suspend fun getNews(page: Int) =
        newsService.getTopHeadlines(page = page)

    suspend fun searchNews(searchQuery: String, page: Int) =
        newsService.getEverything(query = searchQuery, page = page)

    fun getAllFavouriteArticles() = articleDao.getAllArticles()

    suspend fun saveArticle(article: Article) = articleDao.insertArticle(article)

    suspend fun deleteArticle(article: Article) = articleDao.deleteArticle(article)

}