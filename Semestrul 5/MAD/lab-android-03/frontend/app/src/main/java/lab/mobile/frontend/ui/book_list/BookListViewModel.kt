package lab.mobile.frontend.ui.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lab.mobile.frontend.domain.model.Book
import lab.mobile.frontend.domain.repository.BookRepository

class BookListViewModel(
    private val repository: BookRepository
) : ViewModel() {

    val books = repository.getAllBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refreshBooks()
            } catch (e: Exception) {
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}