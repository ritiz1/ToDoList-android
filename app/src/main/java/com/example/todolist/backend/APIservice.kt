package com.example.todolist.backend

import com.example.todolist.models.LoginRequest
import com.example.todolist.models.Note
import com.example.todolist.models.RegisterRequest
import com.example.todolist.models.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object APIservice{
    // USing 10.0.2.2 For android emulator
    private const val BASE_URL = "http://10.0.2.2:8000/api/"
    private val client = HttpClient(Android) {
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys= true
                prettyPrint= true
                isLenient= true
            })
        }
        install(Logging)
    }



    //Now we are going to make a login function
    suspend fun login(loginRequest: LoginRequest): TokenResponse{
        return client.post("${BASE_URL }token/"){

            setBody(loginRequest)
            header("Content-Type","application/json")
        }.body()
    }

    //NOw fetching the notes function
    suspend fun getNotes(token:String) : List<Note>{
        return client.get("${BASE_URL}notes/") {
            header("Authorization", "Bearer $token")
        }.body()
        }

    // This function is for creating a new note .
    suspend fun createNote(token:String , note:Note): Note{
        return client.post("${BASE_URL}notes/"){
            header("Authorization",token)
            header("Content-Type","application/json")
            setBody(note)
        }.body()
    }


    //For registering a new user

    suspend fun register(register: RegisterRequest){
        return client.post("${BASE_URL}register/"){
            header("Content-Type","application/json")
            setBody(register)
        }.body()


    }

    //For deleting a specific note
    suspend fun deleteNote(token: String, noteId: Int?) {
        return client.delete("${BASE_URL}notes/${noteId}/") {
            header("Authorization","Bearer $token")
            header("Content-Type","application/json")

        }.body()
    }


    }




//Fetch Notes function