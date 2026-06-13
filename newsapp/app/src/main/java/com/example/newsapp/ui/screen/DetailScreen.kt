package com.example.newsapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import com.example.newsapp.data.model.Article
import com.example.newsapp.ui.viewmodel.NewsUiState
import com.example.newsapp.ui.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    url: String,
    viewModel: NewsViewModel,
    onBack: () -> Unit
) {
    val headlinesState by viewModel.headlinesState.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val savedArticles by viewModel.savedArticles.collectAsState()

    val article: Article? = remember(url) {
        val fromHeadlines = if (headlinesState is NewsUiState.Success)
            (headlinesState as NewsUiState.Success).articles.find { it.url == url } else null
        val fromSearch = if (searchState is NewsUiState.Success)
            (searchState as NewsUiState.Success).articles.find { it.url == url } else null
        val fromSaved = savedArticles.find { it.url == url }
        fromHeadlines ?: fromSearch ?: fromSaved
    }

    val isSaved = article?.let { viewModel.isArticleSaved(it) } ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Berita", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    article?.let {
                        IconButton(onClick = {
                            if (isSaved) viewModel.removeSavedArticle(it)
                            else viewModel.saveArticle(it)
                        }) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Simpan"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        article?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                it.urlToImage?.let { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(
                    text = it.source?.name ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it.title ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dipublikasikan: ${it.publishedAt?.take(10) ?: "-"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it.author?.let { a -> "Penulis: $a" } ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it.content ?: it.description ?: "Konten tidak tersedia.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } ?: Box(modifier = Modifier.fillMaxSize()) {
            Text("Artikel tidak ditemukan")
        }
    }
}