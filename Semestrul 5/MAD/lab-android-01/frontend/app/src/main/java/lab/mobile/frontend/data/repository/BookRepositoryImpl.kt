package lab.mobile.frontend.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import lab.mobile.frontend.data.remote.BookApi
import lab.mobile.frontend.data.remote.BookDto
import lab.mobile.frontend.domain.model.Book
import lab.mobile.frontend.domain.repository.BookRepository

class BookRepositoryImpl(
    private val api: BookApi
) : BookRepository {

    override fun getAllBooks(): Flow<List<Book>> = flow {
        val remoteBooks = api.getBooks()
        emit(remoteBooks.map { it.toDomainModel() })
    }


    override suspend fun getBookById(id: Int): Book? {
        return try {
            api.getBookById(id).toDomainModel()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateBook(id: Int, book: Book) {
        try {
            val dto = BookDto(
                id = book.id,
                title = book.title,
                author = book.author,
                isNobelPrizeWinner = book.isNobelPrizeWinner,
                publicationDate = book.publicationYear
            )
            val respunse = api.updateBook(id, dto)

        } catch (e: Exception) {
            android.util.Log.e("REPO_ERROR", "Failed to update book: ${e.message}")
            throw e
        }
    }

    private fun BookDto.toDomainModel(): Book {
        return Book(
            id = id,
            title = title,
            author = author,
            isNobelPrizeWinner = isNobelPrizeWinner,
            publicationYear = publicationDate
        )
    }
}