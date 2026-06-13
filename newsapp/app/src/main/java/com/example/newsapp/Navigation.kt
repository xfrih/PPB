package com.example.newsapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsapp.ui.screen.DetailScreen
import com.example.newsapp.ui.screen.HomeScreen
import com.example.newsapp.ui.screen.SavedScreen
import com.example.newsapp.ui.screen.SearchScreen
import com.example.newsapp.ui.viewmodel.NewsViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NewsNavHost(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onArticleClick = { url ->
                    val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$encoded")
                }
            )
        }

        composable("search") {
            SearchScreen(
                viewModel = viewModel,
                onArticleClick = { url ->
                    val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$encoded")
                }
            )
        }

        composable("saved") {
            SavedScreen(
                viewModel = viewModel,
                onArticleClick = { url ->
                    val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$encoded")
                }
            )
        }

        composable("detail/{url}") { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("url") ?: ""
            val url = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
            DetailScreen(
                url = url,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}