package com.example.todolist.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.backend.APIservice
import com.example.todolist.models.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CreatePostViewModel(private val apiService: APIservice): ViewModel() {
    //The state hub . What are the state possibles ? we use sealed class for that..
    private val _createPostState = MutableStateFlow<CreatePostState>(CreatePostState.Idle)
    val createPostState: StateFlow<CreatePostState> = _createPostState


    fun postNote(token: String, note: Note) {
        //1.. first we set the state to loading
        _createPostState.value = CreatePostState.Loading



        viewModelScope.launch {
            try {

                //2. we try to post the note.
                apiService.createNote(token, note)
                //3. if success we update the state to success
                _createPostState.value = CreatePostState.Success("Note created successfully")

            } catch (e: Exception) {
                //4. if error we update the state to error
                _createPostState.value =
                    CreatePostState.Error(e.message ?: "An unexpected error occurred")
            }
        }


    }
}


sealed class CreatePostState{
    object Idle: CreatePostState()
    object Loading: CreatePostState()
    data class Success(val message: String): CreatePostState()
    data class Error(val message: String): CreatePostState()
}