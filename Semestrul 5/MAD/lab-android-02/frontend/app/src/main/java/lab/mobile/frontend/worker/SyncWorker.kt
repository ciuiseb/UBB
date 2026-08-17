package lab.mobile.frontend.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import lab.mobile.frontend.BookApplication
import lab.mobile.frontend.data.remote.BookDto
import lab.mobile.frontend.data.remote.RetrofitClient.api
import lab.mobile.frontend.domain.model.Book

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as BookApplication
        val container = app.container
        val repo = container.bookRepository
        val offlineDao = container.offlineRequestDao
        val gson = Gson()

        return try {
            val requests = offlineDao.getAllRequests()
            for (request in requests) {
                val book = gson.fromJson(request.bookJson, Book::class.java)

                try {
                    if (request.operationType == "UPDATE") {
                        repo.updateBook(book.id, book)
                    }
                    if (request.operationType == "ADD") {
                        val book = gson.fromJson(request.bookJson, Book::class.java)
                        val dto = BookDto(
                            id = book.id,
                            title = book.title,
                            author = book.author,
                            isNobelPrizeWinner = book.isNobelPrizeWinner,
                            publicationDate = book.publicationYear
                        )

                        api.createBook(dto)
                    }

                    offlineDao.deleteRequest(request.id)

                } catch (e: Exception) {
                    android.util.Log.e("SyncWorker", "Failed to sync book ${book.id}", e)
                }
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}