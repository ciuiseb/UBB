package repository.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.TrainingPlan
import org.hibernate.SessionFactory
import repository.interfaces.TrainingPlanRepository
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root

class TrainingPlanRepositoryImpl(private val sessionFactory: SessionFactory) : TrainingPlanRepository {

    override suspend fun findById(id: Long): TrainingPlan? = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            session.get(TrainingPlan::class.java, id)
        }
    }

    override suspend fun findAll(): List<TrainingPlan> = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<TrainingPlan> = criteriaBuilder.createQuery(TrainingPlan::class.java)
            val root: Root<TrainingPlan> = criteriaQuery.from(TrainingPlan::class.java)

            criteriaQuery.select(root)

            session.createQuery(criteriaQuery).list()
        }
    }

    override suspend fun save(entity: TrainingPlan): TrainingPlan = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val savedEntity = session.merge(entity) as TrainingPlan
                transaction.commit()
                savedEntity
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            }
        }
    }

    override suspend fun update(entity: TrainingPlan): TrainingPlan = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val updatedEntity = session.merge(entity) as TrainingPlan
                transaction.commit()
                updatedEntity
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            }
        }
    }

    override suspend fun delete(entity: TrainingPlan): Boolean = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val managedEntity = session.merge(entity)
                session.delete(managedEntity)
                transaction.commit()
                true
            } catch (e: Exception) {
                transaction.rollback()
                false
            }
        }
    }

    override suspend fun deleteById(id: Long): Boolean = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val entity = session.get(TrainingPlan::class.java, id)
                if (entity != null) {
                    session.delete(entity)
                    transaction.commit()
                    true
                } else {
                    transaction.rollback()
                    false
                }
            } catch (e: Exception) {
                transaction.rollback()
                false
            }
        }
    }

    override suspend fun count(): Long = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<Long> = criteriaBuilder.createQuery(Long::class.java)
            val root: Root<TrainingPlan> = criteriaQuery.from(TrainingPlan::class.java)

            criteriaQuery.select(criteriaBuilder.count(root))

            session.createQuery(criteriaQuery).singleResult ?: 0L
        }
    }

    override suspend fun findByUserId(userId: Long): List<TrainingPlan> = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val criteriaBuilder: CriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: CriteriaQuery<TrainingPlan> = criteriaBuilder.createQuery(TrainingPlan::class.java)
            val root: Root<TrainingPlan> = criteriaQuery.from(TrainingPlan::class.java)

            criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get<Long>("userId"), userId))

            session.createQuery(criteriaQuery).list()
        }
    }
}