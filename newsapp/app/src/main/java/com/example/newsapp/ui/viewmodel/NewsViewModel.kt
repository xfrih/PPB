package com.example.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _headlinesState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val headlinesState: StateFlow<NewsUiState> = _headlinesState

    private val _searchState = MutableStateFlow<NewsUiState?>(null)
    val searchState: StateFlow<NewsUiState?> = _searchState

    private val _savedArticles = MutableStateFlow<List<Article>>(emptyList())
    val savedArticles: StateFlow<List<Article>> = _savedArticles

    init {
        loadHeadlines()
    }

    fun loadHeadlines() {
        viewModelScope.launch {
            _headlinesState.value = NewsUiState.Loading
            try {
                val articles = repository.getTopHeadlines()
                _headlinesState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _headlinesState.value = NewsUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch {
            _searchState.value = NewsUiState.Loading
            try {
                val articles = repository.searchNews(query)
                _searchState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _searchState.value = NewsUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun saveArticle(article: Article) {
        val current = _savedArticles.value.toMutableList()
        if (current.none { it.url == article.url }) {
            current.add(article)
            _savedArticles.value = current
        }
    }

    fun removeSavedArticle(article: Article) {
        _savedArticles.value = _savedArticles.value.filter { it.url != article.url }
    }

    fun isArticleSaved(article: Article): Boolean {
        return _savedArticles.value.any { it.url == article.url }
    }
}