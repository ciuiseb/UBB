package lab.mobile.frontend.ui.book_add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lab.mobile.frontend.domain.model.Book
import lab.mobile.frontend.domain.repository.BookRepository
import kotlin.random.Random

class BookAddViewModel(private val repository: BookRepository) : ViewModel() {

    fun addBook(title: String, author: String, year: Int) {
        viewModelScope.launch {
            val newBook = Book(
                id = Random.nextInt(0, Int.MAX_VALUE),
                title = title,
                author = author,
                isNobelPrizeWinner = false,
                publicationDate = year.toString()
            )
            repository.save(book = newBook);
        }
    }
}