package repository.interfaces

import model.User
import model.enums.FitnessLevel
import model.enums.UserRole

interface UserRepository : Repository<User, Long> {
    suspend fun findByUsername(username: String): User?
}