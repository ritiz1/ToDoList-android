package com.example.todolist.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.backend.APIservice
import com.example.todolist.models.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val apiService : APIservice): ViewModel(){
    //1. The state Hub
    // we use sealed class to represent different states possible UI states.
    private val _notesState = MutableStateFlow<NotesState>(NotesState.Loading)
    val notesState : StateFlow<NotesState> = _notesState
    fun fetchNotes(token:String){
        //2.first we start with the loading state to show for the user.
        _notesState.value= NotesState.Loading

        // NOw we will try to fetch the data .
        try{
            viewModelScope.launch{
                val notes= apiService.getNotes(token)
                //3. if sucess we update the state to success.
                _notesState.value= NotesState.Success(notes)

            }
        } catch (e:Exception){
            //4. if error we update the state to error
            _notesState.value= NotesState.Error(e.message ?: "An unexpected error occurred")
        }
    }
}




// We are defining the sealed function here to define 3 different states of screen
sealed class NotesState{
    object Loading : NotesState()
    data class Success(val notes: List<Note>): NotesState()
    data class Error(val message:String): NotesState()
}