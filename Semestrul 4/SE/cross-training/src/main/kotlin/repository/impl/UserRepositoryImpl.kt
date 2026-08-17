package repository.impl

import jakarta.persistence.NoResultException
import model.User
import org.hibernate.SessionFactory
import repository.interfaces.UserRepository

class UserRepositoryImpl(private val sessionFactory: SessionFactory) : UserRepository {

    override suspend fun findById(id: Long): User? {
        return sessionFactory.openSession().use { session ->
            session.get(User::class.java, id)
        }
    }

    override suspend fun findAll(): List<User> {
        return sessionFactory.openSession().use { session ->
            session.createQuery("FROM User", User::class.java).list()
        }
    }

    override suspend fun save(entity: User): User {
        return sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                session.persist(entity)
                transaction.commit()
                entity
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            }
        }
    }

    override suspend fun update(entity: User): User {
        return sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val updated = session.merge(entity)
                transaction.commit()
                updated
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            }
        }
    }

    override suspend fun delete(entity: User): Boolean {
        return deleteById(entity.id)
    }

    override suspend fun deleteById(id: Long): Boolean {
        return sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val rowsAffected = session.createQuery("DELETE FROM User WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate()
                transaction.commit()
                rowsAffected > 0
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            }
        }
    }

    override suspend fun count(): Long {
        return sessionFactory.openSession().use { session ->
            session.createQuery("SELECT COUNT(u) FROM User u", Long::class.java).singleResult
        }
    }

    override suspend fun findByUsername(username: String): User? {
        return sessionFactory.openSession().use { session ->
            try {
                session.createQuery("FROM User WHERE username = :username", User::class.java)
                    .setParameter("username", username)
                    .singleResult
            } catch (e: NoResultException) {
                null
            }
        }
    }
}