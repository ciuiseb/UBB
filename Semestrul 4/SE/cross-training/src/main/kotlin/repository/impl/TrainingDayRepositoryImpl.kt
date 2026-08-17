package repository.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.TrainingDay
import org.hibernate.SessionFactory
import org.hibernate.query.criteria.HibernateCriteriaBuilder
import org.hibernate.query.criteria.JpaCriteriaQuery
import org.hibernate.query.criteria.JpaRoot
import repository.interfaces.TrainingDayRepository
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class TrainingDayRepositoryImpl(private val sessionFactory: SessionFactory) : TrainingDayRepository {

    override suspend fun findByTrainingPlanId(trainingPlanId: Long): List<TrainingDay> = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val criteriaBuilder = session.criteriaBuilder
            val criteriaQuery = criteriaBuilder.createQuery(TrainingDay::class.java)
            val root: JpaRoot<TrainingDay> = criteriaQuery.from(TrainingDay::class.java)

            criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get<Long>("trainingPlanId"), trainingPlanId))

            session.createQuery(criteriaQuery).list()
        }
    }

    override suspend fun findByTrainingPlanIdAndDayNumber(trainingPlanId: Long, dayNumber: Int): TrainingDay? =
        withContext(Dispatchers.IO) {
            sessionFactory.openSession().use { session ->
                val criteriaBuilder: HibernateCriteriaBuilder = session.criteriaBuilder
                val criteriaQuery: JpaCriteriaQuery<TrainingDay> = criteriaBuilder.createQuery(TrainingDay::class.java)
                val root: JpaRoot<TrainingDay> = criteriaQuery.from(TrainingDay::class.java)

                criteriaQuery.select(root)
                    .where(
                        criteriaBuilder.and(
                            criteriaBuilder.equal(root.get<Long>("trainingPlanId"), trainingPlanId),
                            criteriaBuilder.equal(root.get<Int>("dayNumber"), dayNumber)
                        )
                    )

                session.createQuery(criteriaQuery)
                    .resultList
                    .firstOrNull()
            }
        }

    override suspend fun findById(id: Long): TrainingDay? = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            session.get(TrainingDay::class.java, id)
        }
    }

    override suspend fun findAll(): List<TrainingDay> = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val criteriaBuilder: HibernateCriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: JpaCriteriaQuery<TrainingDay> = criteriaBuilder.createQuery(TrainingDay::class.java)
            val root: JpaRoot<TrainingDay> = criteriaQuery.from(TrainingDay::class.java)

            criteriaQuery.select(root)

            session.createQuery(criteriaQuery).list()
        }
    }

    override suspend fun save(entity: TrainingDay): TrainingDay = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val savedEntity = session.merge(entity) as TrainingDay
                transaction.commit()
                savedEntity
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            }
        }
    }

    override suspend fun update(entity: TrainingDay): TrainingDay = withContext(Dispatchers.IO) {
        sessionFactory.openSession().use { session ->
            val transaction = session.beginTransaction()
            try {
                val updatedEntity = session.merge(entity) as TrainingDay
                transaction.commit()
                updatedEntity
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            }
        }
    }

    override suspend fun delete(entity: TrainingDay): Boolean = withContext(Dispatchers.IO) {
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
                val entity = session.get(TrainingDay::class.java, id)
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
            val criteriaBuilder: HibernateCriteriaBuilder = session.criteriaBuilder
            val criteriaQuery: JpaCriteriaQuery<Long> = criteriaBuilder.createQuery(Long::class.java)
            val root: JpaRoot<TrainingDay> = criteriaQuery.from(TrainingDay::class.java)

            criteriaQuery.select(criteriaBuilder.count(root))

            session.createQuery(criteriaQuery).singleResult ?: 0L
        }
    }
}