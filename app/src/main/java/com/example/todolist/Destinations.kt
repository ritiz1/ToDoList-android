package com.example.todolist

interface Destinations {
     val route:String
}
object Login :Destinations{
    override val route:String = "login"
}

object Home : Destinations {
    override val route: String= "home"
}


object CreateNote: Destinations {
    override val route: String= "create_note"
}