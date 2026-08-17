package lab.mobile.frontend

import android.content.Context
import lab.mobile.frontend.data.local.dataStore
import lab.mobile.frontend.data.remote.RetrofitClient
import lab.mobile.frontend.data.repository.AuthRepositoryImpl
import lab.mobile.frontend.data.repository.BookRepositoryImpl
import lab.mobile.frontend.domain.repository.AuthRepository
import lab.mobile.frontend.domain.repository.BookRepository
import androidx.room.Room

class AppContainer(private val context: Context) {
    init {
        RetrofitClient.dataStore = context.dataStore
    }
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            api = RetrofitClient.authApi,
            dataStore = context.dataStore
        )
    }

    val bookRepository: BookRepository by lazy {
        BookRepositoryImpl(
            api = RetrofitClient.api
        )
    }
}