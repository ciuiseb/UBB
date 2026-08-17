package lab.mobile.frontend.domain.repository

import lab.mobile.frontend.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): User
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}