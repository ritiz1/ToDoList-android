package com.example.todolist

import CreatePostScreen
import NoteScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.frontend.LoginScreen
import com.example.todolist.frontend.ProfileScreen
import com.example.todolist.frontend.RegisterScreen
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AppNavigation()
        }
    }
}
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = TokenManager(context)

    val token by tokenManager.token.collectAsState(initial = null)
    val userName by tokenManager.username.collectAsState(initial = null)

    val startDestination = if (!token.isNullOrEmpty()) Home.route else Login.route
    var currentRoute by remember { mutableStateOf(startDestination) }

    Scaffold(
        bottomBar = {
            if (!token.isNullOrEmpty()) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == Home.route,
                        onClick = { navController.navigate(Home.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        selected = currentRoute == Profile.route,
                        onClick = { navController.navigate(Profile.route) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(paddingValues)
        ) {
            composable(Login.route) { currentRoute = Login.route; LoginScreen(navController) }
            composable(Home.route) { currentRoute = Home.route; NoteScreen(token ?: "", navController) }
            composable(CreateNote.route) { CreatePostScreen(navController, token ?: "", onNoteCreated = {
                navController.navigate(Home.route) {
                    popUpTo(CreateNote.route) { inclusive = true }
                }
            }) }
            composable(Register.route) { RegisterScreen(onRegister =
                {navController.navigate(Home.route) {
                    popUpTo(Register.route) { inclusive = true }
                }

                }, navigation = navController) }
            composable(Profile.route) { currentRoute = Profile.route; ProfileScreen(navController, userName ?: "") }
        }
    }
}
