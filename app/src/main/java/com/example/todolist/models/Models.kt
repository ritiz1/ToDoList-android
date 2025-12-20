package com.example.todolist.models
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int? = null,
    val title: String,
    val body: String="",
    val created_at: String?= null
)
@Serializable
data class TokenResponse(
    val access: String,
    val refresh: String
)
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val username: String ,
    val email: String ,
    val password: String
)