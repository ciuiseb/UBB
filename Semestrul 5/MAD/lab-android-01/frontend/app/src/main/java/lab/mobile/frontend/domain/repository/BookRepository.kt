package lab.mobile.frontend.domain.repository

import kotlinx.coroutines.flow.Flow
import lab.mobile.frontend.domain.model.Book

interface BookRepository {
    fun getAllBooks(): Flow<List<Book>>

    suspend fun getBookById(id: Int): Book?

    suspend fun updateBook(id: Int, book: Book)
}