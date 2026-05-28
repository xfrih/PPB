package com.example.loginmvvmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loginmvvmapp.data.AppDatabase
import com.example.loginmvvmapp.data.UserRepository
import com.example.loginmvvmapp.ui.LoginScreen
import com.example.loginmvvmapp.ui.LoginViewModel
import com.example.loginmvvmapp.ui.LoginViewModelFactory
import com.example.loginmvvmapp.ui.theme.LoginMVVMAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = UserRepository(database.userDao())
        val factory = LoginViewModelFactory(repository)

        setContent {
            LoginMVVMAppTheme {
                val loginViewModel: LoginViewModel = viewModel(factory = factory)
                loginViewModel.insertDummyUser()
                LoginScreen(viewModel = loginViewModel)
            }
        }
    }
}