package com.example.newsapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.api.NewsService
import com.example.newsapp.models.Article
import retrofit2.HttpException
import javax.inject.Inject

class NewsPagingSource @Inject constructor (
    private val newsService: NewsService,
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        val response = newsService.getTopHeadlinesPaging(page = page, pageSize = pageSize)
        if (response.isSuccessful) {
            val articles = checkNotNull(response.body()).articles
            val nextKey = if (articles.size < pageSize) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            return LoadResult.Page(
                data = articles,
                nextKey = nextKey,
                prevKey = prevKey
            )
        } else {
            return LoadResult.Error(HttpException(response))
        }
    }

}