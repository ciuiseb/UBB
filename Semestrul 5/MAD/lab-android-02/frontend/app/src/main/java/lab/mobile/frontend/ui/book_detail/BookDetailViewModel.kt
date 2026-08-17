package lab.mobile.frontend.ui.book_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.mobile.frontend.domain.model.Book
import lab.mobile.frontend.domain.repository.BookRepository

class BookDetailViewModel(
    private val repository: BookRepository,
    private val bookId: Int
) : ViewModel() {

    var book by mutableStateOf<Book?>(null)
    var isEditing by mutableStateOf(false)
    var titleField by mutableStateOf("")

    var snackbarMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            val result = repository.getBookById(bookId)
            book = result
            titleField = result?.title ?: ""
        }
    }

    fun save() {
        viewModelScope.launch {
            book?.let {
                val updated = it.copy(title = titleField)

                try {
                    repository.updateBook(bookId, updated)

                    snackbarMessage = "Book updated successfully!"
                } catch (e: Exception) {
                    snackbarMessage = "Offline: Saved to local storage."
                }

                book = updated
                isEditing = false
            }
        }
    }
    fun clearSnackbarMessage() {
        snackbarMessage = null
    }
}