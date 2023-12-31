package com.example.newsapp.data.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("/v2/top-headlines")
    suspend fun getTopHeadlinesPaging(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("country") countryCode: String = "us",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): Response<NewsResponse>

}