package com.example.gamingspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.gamingspace.data.database.AppDatabase
import com.example.gamingspace.data.repository.GamingRepository
import com.example.gamingspace.ui.viewmodel.GamingViewModel
import com.example.gamingspace.ui.viewmodel.GamingViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val repository = GamingRepository(database.memberDao(), database.transactionDao())
        val factory = GamingViewModelFactory(repository)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF0D0D0D)
            ) {
                val navController = rememberNavController()
                val viewModel: GamingViewModel = viewModel(factory = factory)
                GamingNavHost(navController = navController, viewModel = viewModel)
            }
        }
    }
}