package com.example.newsapp.data.repository

import com.example.newsapp.data.model.Article
import com.example.newsapp.data.remote.RetrofitInstance

class NewsRepository {

    private val api = RetrofitInstance.api

    private val apiKey = "cd2dd33cf4f140d6b771a455178035bb"

    suspend fun getTopHeadlines(): List<Article> {
        return api.getTopHeadlines(apiKey = apiKey).articles
    }

    suspend fun searchNews(query: String): List<Article> {
        return api.searchNews(query = query, apiKey = apiKey).articles
    }
}