package lab.mobile.frontend.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import lab.mobile.frontend.BookApplication
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