package lab.mobile.frontend.data.repository

import android.content.Context
import androidx.work.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import lab.mobile.frontend.data.local.BookDao
import lab.mobile.frontend.data.local.OfflineRequest
import lab.mobile.frontend.data.local.OfflineRequestDao
import lab.mobile.frontend.data.remote.BookApi
import lab.mobile.frontend.data.remote.BookDto
import lab.mobile.frontend.domain.model.Book
import lab.mobile.frontend.domain.repository.BookRepository
import lab.mobile.frontend.worker.SyncWorker

class BookRepositoryImpl(
    private val api: BookApi,
    private val dao: BookDao,
    private val offlineDao: OfflineRequestDao,
    context: Context
) : BookRepository {

    private val gson = Gson()
    private val workManager = WorkManager.getInstance(context)

    override fun getAllBooks(): Flow<List<Book>> {
        return dao.getAllBooks()
    }

    override suspend fun getBookById(id: Int): Book? {
        return dao.getBook(id)
    }

    override suspend fun updateBook(id: Int, book: Book) {

        dao.insert(book)

        try {
            val dto = BookDto(
                id = book.id,
                title = book.title,
                author = book.author,
                isNobelPrizeWinner = book.isNobelPrizeWinner,
                publicationDate = book.publicationYear
            )


            val response = api.updateBook(id, dto)
            if (!response.isSuccessful) {
                throw Exception("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            android.util.Log.e("REPO_ERROR", "Offline/Sync failed: ${e.message}")


            val json = gson.toJson(book)
            val request = OfflineRequest(operationType = "UPDATE", bookJson = json)
            offlineDao.insert(request)

            val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            workManager.enqueue(syncRequest)

            throw e
        }
    }

    override suspend fun refreshBooks() {
        try {
            val remoteBooks = api.getBooks()
            val domainBooks = remoteBooks.map { it.toDomainModel() }
            dao.insertAll(domainBooks)
        } catch (e: Exception) {
            android.util.Log.e("REPO_ERROR", "Offline: ${e.message}")
            throw e
        }
    }

    private fun BookDto.toDomainModel(): Book {
        return Book(
            id = id,
            title = title,
            author = author,
            isNobelPrizeWinner = isNobelPrizeWinner,
            publicationDate = publicationDate
        )
    }
}