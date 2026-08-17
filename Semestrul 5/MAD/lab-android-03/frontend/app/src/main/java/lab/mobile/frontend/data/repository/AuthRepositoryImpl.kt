package lab.mobile.frontend.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import lab.mobile.frontend.data.remote.AuthApi
import lab.mobile.frontend.domain.model.User
import lab.mobile.frontend.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import lab.mobile.frontend.data.remote.LoginRequest

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    override suspend fun login(username: String, password: String): User {
        val response = api.login(LoginRequest(username, password))

        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = response.token
        }

        return User(username = username, token = response.token)
    }

    override suspend fun isLoggedIn(): Boolean {
        val token = dataStore.data.map { it[TOKEN_KEY] }.first()
        return !token.isNullOrBlank()
    }

    override suspend fun logout() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }
}