package com.example.todolist

import CreatePostScreen
import NoteScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.frontend.AfterLoggedIn
import com.example.todolist.frontend.LoginScreen
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
fun AppNavigation(modifier:Modifier= Modifier){
    val navController = rememberNavController()
    val context= LocalContext.current
    val tokenManager= TokenManager(context)

    //COllecting the token from DataStore
    val token by tokenManager.token.collectAsState(initial= null)

    val startDestination = if(!token.isNullOrEmpty()) {
        Home.route
    } else {
        Login.route
    }


    NavHost(
        navController= navController,
        startDestination= startDestination,
        modifier= modifier
    ){
        composable(Login.route) {
            LoginScreen(navController = navController)
        }
        composable (Home.route){
            NoteScreen(
                navController = navController,
                token = token ?: ""
            )
        }
        composable(CreateNote.route){
            CreatePostScreen(
                navController= navController,
                token= token ?: "",
                onNoteCreated = {
                    navController.navigate(Home.route){
                        popUpTo(CreateNote.route){
                            inclusive= true
                        }
                    }
                }
            )
        }
    }

}