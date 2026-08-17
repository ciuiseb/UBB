package lab.mobile.frontend.ui.bookdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel,
    onBack: () -> Unit
) {
    val book = viewModel.book ?: return

    Column(Modifier.padding(16.dp)) {
        if (viewModel.isEditing) {
            OutlinedTextField(
                value = viewModel.titleField,
                onValueChange = { viewModel.titleField = it },
                label = { Text("Title") }
            )
            Button(onClick = { viewModel.save() }) { Text("Save") }
        } else {
            Text("Title: ${book.title}" )
            Button(onClick = { viewModel.isEditing = true }) { Text("Edit") }
        }

        TextButton(onClick = onBack) { Text("Back") }
    }
}