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

@Composable
fun LoginScreen(navController: NavController) {

    val context= LocalContext.current
    val tokenManager= remember{ TokenManager(context) }

    val viewModel: LoginScreenViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoginScreenViewModel(APIservice, tokenManager) as T
            }
        }
    )
    var userName by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }

    //LaunchedEffect is used to observe the login state changes .
    //Launched Effect  is the function that runs code when the screen loads or when a value changes .

    LaunchedEffect(viewModel.loginState.value) {
        if(viewModel.loginState.value is LoginState.Success){
            navController.navigate(Home.route){
                popUpTo(Login.route){
                    inclusive= true
                }
            }

        }
    }
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        OutlinedTextField(
            value= userName,
            onValueChange = { userName=it},
            label = { Text("Username") },

            modifier = Modifier.fillMaxWidth()
                .padding(bottom= 20.dp)
        )
        

        OutlinedTextField(
            value= password,
            onValueChange = {password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
                .padding(bottom= 20.dp)
        )


//Show loading or error
        when (val state= viewModel.loginState.value){
            is LoginState.Error -> {
                Text(
                    text= state.message,
                    color= Color.Red
                )
            }
            is LoginState.Loading ->{
                CircularProgressIndicator(modifier= Modifier.padding(16.dp))

            }
            else -> {}
        }
        Button(
            onClick = { viewModel.login(userName, password) },
            enabled = viewModel.loginState.value !is LoginState.Loading
        ) {
            Text(text = "Login")
        }
    }



}
//
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen()
//}