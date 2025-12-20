package com.example.todolist.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.backend.APIservice
import com.example.todolist.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterScreenViewModel(private val apiService: APIservice): ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(register: RegisterRequest) {
        _registerState.value = RegisterState.Loading
        if (register.username.isBlank() || register.password.isBlank() || register.email.isBlank()) {
            _registerState.value = RegisterState.Error("All fields are required")
            return
        }
        viewModelScope.launch {
            try {
                apiService.register(register)
                _registerState.value = RegisterState.Success("Registration Successful")
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Registration Failed")

            }

        }
    }
}
sealed class RegisterState{
    object Idle: RegisterState()
    object Loading: RegisterState()
    data class Success(val message: String): RegisterState()
    data class Error(val error: String): RegisterState()
}