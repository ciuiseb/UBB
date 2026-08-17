package lab.mobile.frontend

import android.content.Context
import androidx.room.Room
import lab.mobile.frontend.data.local.BookDatabase
import lab.mobile.frontend.data.local.OfflineRequestDao // Import this
import lab.mobile.frontend.data.local.dataStore
import lab.mobile.frontend.data.remote.RetrofitClient
import lab.mobile.frontend.data.repository.AuthRepositoryImpl
import lab.mobile.frontend.data.repository.BookRepositoryImpl
import lab.mobile.frontend.domain.repository.AuthRepository
import lab.mobile.frontend.domain.repository.BookRepository

class AppContainer(private val context: Context) {
    init {
        RetrofitClient.dataStore = context.dataStore
    }

    private val database: BookDatabase by lazy {
        BookDatabase.getDatabase(context)
    }

    val offlineRequestDao: OfflineRequestDao by lazy {
        database.offlineRequestDao()
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            api = RetrofitClient.authApi,
            dataStore = context.dataStore
        )
    }

    val bookRepository: BookRepository by lazy {
        BookRepositoryImpl(
            api = RetrofitClient.api,
            dao = database.bookDao(),
            offlineDao = offlineRequestDao,
            context = context
        )
    }
}