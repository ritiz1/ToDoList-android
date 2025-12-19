package com.example.todolist.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.backend.APIservice
import com.example.todolist.models.LoginRequest
import com.example.todolist.utils.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class LoginScreenViewModel(
    private val apiService: APIservice= APIservice,
    private val tokenManager: TokenManager
): ViewModel() {

    private val _loginState = mutableStateOf<LoginState>(LoginState.Idle)
    val loginState = _loginState

    //Checking if user is already logged in
    init {
        viewModelScope.launch {
            // Here you can check if the token exists in DataStore and update the loginState accordingly

            val savedToken = tokenManager.token.firstOrNull()
            if (savedToken != null) {
                _loginState.value = LoginState.Success(savedToken)
            }
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Username or Password cannot be empty")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {

                val loginRequest = LoginRequest(username = username, password = password)
                val response = apiService.login(loginRequest)

                //saving token to the datastore
                tokenManager.saveToken(response.access)

                _loginState.value = LoginState.Success(response.access)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login Failed")
            }
        }
    }
    fun logout(){
            viewModelScope.launch {
            tokenManager.clearToken()
            _loginState.value= LoginState.Idle
        }
    }
}



//Defining the LoginState sealed class. We need this because we need ui to know if we are loading, successful , or error occured.
sealed class LoginState{
    object Idle: LoginState()
    object Loading: LoginState()
    data class Success(val token: String): LoginState()
    data class Error (val message: String): LoginState()
}