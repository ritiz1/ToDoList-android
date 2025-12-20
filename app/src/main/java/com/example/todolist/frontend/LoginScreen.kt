package com.example.todolist.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.Home
import com.example.todolist.Login
import com.example.todolist.backend.APIservice
import com.example.todolist.models.LoginRequest
import com.example.todolist.utils.TokenManager
import com.example.todolist.viewModels.LoginScreenViewModel
import com.example.todolist.viewModels.LoginState
import androidx.compose.runtime.collectAsState
import com.example.todolist.Register

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    // 1. Move the Factory or use a cleaner approach
    val viewModel: LoginScreenViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return LoginScreenViewModel(APIservice, tokenManager) as T
            }
        }
    )

    // 2. COLLECT ONCE AT THE TOP
    // Use collectAsStateWithLifecycle() if you have the lifecycle-runtime-compose dependency (standard in 2025)
    val loginState by viewModel.loginState.collectAsState()

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 3. CLEAN LAUNCHEDEFFECT
    // We react when the loginState changes.
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            tokenManager.saveUsername(userName)
            navController.navigate(Home.route) {
                popUpTo(Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        )

        // 4. USE THE DELEGATED STATE (No .value everywhere)
        when (val state = loginState) {
            is LoginState.Error -> {
                Text(text = state.message, color = Color.Red)
            }
            is LoginState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            else -> {}
        }

        Button(
            onClick = { viewModel.login(userName, password) },
            enabled = loginState !is LoginState.Loading // Clean comparison
        ) {
            Text(text = "Login")
        }

        // Bottom action to navigate to Register screen
        TextButton(onClick = {
            navController.navigate(Register.route)
        }) {
            Text(text = "Don’t have an account? Create one.")
        }


    }
}