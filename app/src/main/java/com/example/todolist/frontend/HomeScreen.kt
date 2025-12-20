import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.CreateNote
import com.example.todolist.Profile
import com.example.todolist.backend.APIservice
import com.example.todolist.models.Note
import com.example.todolist.viewModels.HomeScreenViewModel
import com.example.todolist.viewModels.NotesState


@Composable
fun NoteScreen(token:String,navController: NavController){

    //First defining the viewmodel
    val viewModel: HomeScreenViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeScreenViewModel(APIservice) as T
            }
        }
    )

    //we are going to pass the token in the navigation side.
    //1 . First we have to observe the state that we are in .
     //collectAsState() is being used here to convert the flow into a compose state that autoupdates the UI.
    val uiState by viewModel.notesState.collectAsState()


    //2, fetch data on start
    // We use LaunchedEffect to fetch notes . It runs only once when the screen is first created;
    LaunchedEffect(Unit) {
        viewModel.fetchNotes(token)

    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {navController.navigate(CreateNote.route)}) {
                Text("+")
            }
        }
    ) {

        paddingValues ->


            // NOw lets define UI logic switch
            // Since the uiState is a sealed class, 'when' forces us to handle every cases .
            when (val state = uiState) {

                //State 1. Loading
                is NotesState.Loading -> {
                    // Show loading indicator
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is NotesState.Error -> {
                    //Showing the error message.
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.message, color = Color.Red)
                    }
                }

                ///FOR SUCCESS

                is NotesState.Success -> {
                    //Show list of the notes
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(state.notes) { note ->
                            NoteCard(note = note, onDeleteClick = {
                                viewModel.clearNotes(token, note.id)


                            })
                        }

                    }






            }
        }
    }

}


//This is for the notecard for each of the boxes in the to do list .
@Composable
fun NoteCard(note: Note, onDeleteClick: () -> Unit = {}) {
    Card(
        modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp , vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {


            Column(modifier = Modifier.padding(16.dp)
                .weight(1f)) {

                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = note.body,
                    style = MaterialTheme.typography.bodyMedium
                )


            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Note",
                    tint = Color.Red
                )
            }
        }

    }

}