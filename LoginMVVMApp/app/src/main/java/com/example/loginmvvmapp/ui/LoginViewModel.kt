package com.example.loginmvvmapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginmvvmapp.data.User
import com.example.loginmvvmapp.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(username, password)
            if (user != null) {
                _loginState.value = "Login Berhasil"
            } else {
                _loginState.value = "Username atau Password Salah"
            }
        }
    }

    fun insertDummyUser() {
        viewModelScope.launch {
            repository.insert(User(username = "admin", password = "12345"))
        }
    }
}