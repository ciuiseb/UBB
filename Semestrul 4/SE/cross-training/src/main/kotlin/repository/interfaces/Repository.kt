package repository.interfaces

interface Repository<T, ID> {
    suspend fun findById(id: ID): T?
    suspend fun findAll(): List<T>
    suspend fun save(entity: T): T
    suspend fun update(entity: T): T
    suspend fun delete(entity: T): Boolean
    suspend fun deleteById(id: ID): Boolean
    suspend fun count(): Long
}