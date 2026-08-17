package lab.mobile.frontend.ui.bookdetail

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
                repository.updateBook(bookId, updated)
                book = updated
                isEditing = false
            }
        }
    }
}