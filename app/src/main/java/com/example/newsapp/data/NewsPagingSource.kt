package com.example.newsapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.api.NewsService
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import javax.inject.Inject

class NewsPagingSource @Inject constructor (
    private val newsService: NewsService,
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1
            val response = newsService.getTopHeadlines(page = page).body()?.articles ?: emptyList()
            val nextKey = if (response.isEmpty()) null else response.size.plus(page).plus(1)
            val prevKey = if (page == 1) null else response.size.minus(1)
            LoadResult.Page(
                data = response,
                nextKey = nextKey,
                prevKey = prevKey
            )

        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }

}