import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolist.backend.APIservice
import com.example.todolist.models.Note
import com.example.todolist.viewModels.CreatePostState
import com.example.todolist.viewModels.CreatePostViewModel
//import com.example.todolist.viewModels.HomeScreenViewModel

@Composable
fun CreatePostScreen(
    navController: NavController,
    token: String,
    onNoteCreated: () -> Unit = {}
) {
    val viewModel: CreatePostViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CreatePostViewModel(APIservice) as T
            }
        }
    )
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val createPostState by viewModel.createPostState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Create New Note",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 10
        )

        Button(
            onClick = {
                val note = Note(title = title, body = description
                )
                viewModel.postNote("Bearer $token", note)
            },
            modifier = Modifier.align(Alignment.End),
            enabled = title.isNotBlank() && createPostState !is CreatePostState.Loading
        ) {
            if (createPostState is CreatePostState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Create Note")
            }
        }

        when (val state = createPostState) {
            is CreatePostState.Success -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.primary
                )
                LaunchedEffect(Unit) {
                    onNoteCreated()
                }
            }
            is CreatePostState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {}
        }
    }
}
