package repository.interfaces

import model.TrainingDay

interface TrainingDayRepository: Repository<TrainingDay, Long> {
    suspend fun findByTrainingPlanId(trainingPlanId: Long): List<TrainingDay>
    suspend fun findByTrainingPlanIdAndDayNumber(trainingPlanId: Long, dayNumber: Int): TrainingDay?
}