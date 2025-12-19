package com.example.todolist.frontend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.utils.TokenManager
import kotlinx.coroutines.launch

@Composable
fun AfterLoggedIn(navController: NavController, username: String) {
    val context = LocalContext.current
    val tokenManager= TokenManager(context)
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Logged in broooo!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Hi, $username",
            style = MaterialTheme.typography.headlineSmall
        )
        Button (onClick = {

                scope.launch{
                    tokenManager.clearToken()
                    navController.navigate("login"){
                        popUpTo("home"){
                            inclusive= true
                        }
                    }

            }

        })
        {
            // Handle logout act{
            Text(text = "Logout")
        }
    }
}
