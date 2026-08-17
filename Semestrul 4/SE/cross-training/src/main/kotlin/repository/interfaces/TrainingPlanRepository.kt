package repository.interfaces

import model.TrainingPlan
import model.enums.FitnessLevel

interface TrainingPlanRepository : Repository<TrainingPlan, Long> {
    suspend fun findByUserId(userId: Long): List<TrainingPlan>
}