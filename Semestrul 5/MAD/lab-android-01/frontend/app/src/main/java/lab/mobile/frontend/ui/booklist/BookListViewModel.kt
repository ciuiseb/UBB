package lab.mobile.frontend.ui.booklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.mobile.frontend.domain.model.Book
import lab.mobile.frontend.domain.repository.BookRepository

class BookListViewModel(
    private val repository: BookRepository
) : ViewModel() {

    var books by mutableStateOf<List<Book>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        android.util.Log.d("DEBUG_RACE", "3. BookListViewModel initialized. Calling loadBooks()...")
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.getAllBooks().collect { bookList ->
                    books = bookList
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load books: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}