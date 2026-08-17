package lab.mobile.repositories

import lab.mobile.model.Book
import lab.mobile.model.Books
import org.jetbrains.exposed.sql.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class BookRepository {
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun getAllBooks(): List<Book> = dbQuery {
        Books.selectAll().map { toBook(it) }
    }

    suspend fun addBook(book: Book): Int = dbQuery {
        Books.insert {
            it[title] = book.title
            it[author] = book.author
            it[isNobelPrizeWinner] = book.isNobelPrizeWinner
            it[publicationDate] = book.publicationDate
        } get Books.id
    }

    suspend fun updateBook(id: Int, book: Book): Boolean = dbQuery {
        val rowsUpdated = Books.update({ Books.id eq id }) {
            it[title] = book.title
            it[author] = book.author
            it[isNobelPrizeWinner] = book.isNobelPrizeWinner
            it[publicationDate] = book.publicationDate
        }
        rowsUpdated > 0
    }

    suspend fun deleteBook(id: Int): Boolean = dbQuery {
        val rowsDeleted = Books.deleteWhere { Books.id eq id }
        rowsDeleted > 0
    }

    suspend fun getBookById(id: Int): Book? = dbQuery {
        Books.selectAll()
            .where { Books.id eq id }
            .map { toBook(it) }
            .singleOrNull()
    }

    private fun toBook(row: ResultRow) = Book(
        id = row[Books.id],
        title = row[Books.title],
        author = row[Books.author],
        isNobelPrizeWinner = row[Books.isNobelPrizeWinner],
        publicationDate = row[Books.publicationDate]
    )
}