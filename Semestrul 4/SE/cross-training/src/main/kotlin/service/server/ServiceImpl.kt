package service.server

import java.time.LocalDate
import model.TrainingDay
import model.TrainingPlan
import model.User
import model.enums.FitnessLevel
import model.requests.FitnessLevelRequest
import model.requests.TrainingPlanRequest
import repository.interfaces.TrainingDayRepository
import repository.interfaces.TrainingPlanRepository
import repository.interfaces.UserRepository
import service.gemini.GeminiService
import java.time.temporal.ChronoUnit
import org.slf4j.LoggerFactory

class ServiceImpl(
    private val userRepository: UserRepository,
    private val trainingPlanRepository: TrainingPlanRepository,
    private val trainingDayRepository: TrainingDayRepository,
    private val geminiService: GeminiService
) : Service {

    private val logger = LoggerFactory.getLogger(ServiceImpl::class.java)

    override suspend fun register(name: String, username: String, email: String, password: String): User? {
        logger.info("Registration attempt for username: {}", username)

        if (userRepository.findByUsername(username) != null) {
            logger.warn("Registration failed - username already exists: {}", username)
            return null
        }

        val user = User(
            name = name,
            username = username,
            email = email,
            password = password.hashCode().toString(),
        )

        return try {
            val savedUser = userRepository.save(user)
            logger.info("User registered successfully: {} (ID: {})", username, savedUser.id)
            savedUser
        } catch (e: Exception) {
            logger.error("Failed to register user {}: {}", username, e.message)
            throw e
        }
    }

    override suspend fun login(username: String, password: String): User? {
        logger.info("Login attempt for username: {}", username)

        val supposedUser = userRepository.findByUsername(username)
        if (supposedUser == null) {
            logger.warn("Login failed - user not found: {}", username)
            return null
        }

        if (supposedUser.password != password.hashCode().toString()) {
            logger.warn("Login failed - invalid password for user: {}", username)
            return null
        }

        logger.info("User logged in successfully: {} (ID: {})", username, supposedUser.id)
        return supposedUser
    }

    override suspend fun getUserTrainingPlan(user: User): TrainingPlan? {
        if (user.fitnessLevel == FitnessLevel.NONE) {
            logger.info("No training plan available - fitness level not set for user: {}", user.id)
            return null
        }

        return try {
            val plan = trainingPlanRepository.findByUserId(user.id).firstOrNull()
            if (plan != null) {
                logger.info("Retrieved training plan '{}' for user: {}", plan.name, user.id)
            } else {
                logger.info("No training plan found for user: {}", user.id)
            }
            plan
        } catch (e: Exception) {
            logger.error("Failed to retrieve training plan for user {}: {}", user.id, e.message)
            throw e
        }
    }

    override suspend fun updateFitnessLevel(user: User, fitnessLevel: FitnessLevel): User {
        logger.info("Updating fitness level to {} for user: {}", fitnessLevel, user.id)

        val updatedUser = user.copy(fitnessLevel = fitnessLevel)

        return try {
            val savedUser = userRepository.update(updatedUser)
            logger.info("Fitness level updated successfully for user: {}", user.id)
            savedUser
        } catch (e: Exception) {
            logger.error("Failed to update fitness level for user {}: {}", user.id, e.message)
            throw e
        }
    }

    override suspend fun updateTrainingPlan(user: User, plan: TrainingPlan): TrainingPlan? {
        if (plan.userId != user.id) {
            logger.warn("Unauthorized training plan update attempt - plan belongs to user {} but requested by user {}",
                plan.userId, user.id)
            return null
        }

        return try {
            val savedPlan = trainingPlanRepository.save(plan)
            logger.info("Training plan '{}' updated for user: {}", savedPlan.name, user.id)
            savedPlan
        } catch (e: Exception) {
            logger.error("Failed to update training plan for user {}: {}", user.id, e.message)
            throw e
        }
    }

    override suspend fun getTodaysWorkout(plan: TrainingPlan): TrainingDay? {
        val today = LocalDate.now()
        if (today.isBefore(plan.startDate) or today.isAfter(plan.endDate)) {
            logger.info("No workout today - plan '{}' not active (today: {}, plan: {} to {})",
                plan.name, today, plan.startDate, plan.endDate)
            return null
        }

        val dayNumber = ChronoUnit.DAYS.between(plan.startDate, today).toInt() + 1

        return try {
            val workout = trainingDayRepository.findByTrainingPlanIdAndDayNumber(plan.id, dayNumber)
            if (workout != null) {
                logger.info("Retrieved today's workout (day {}) for plan '{}': {}",
                    dayNumber, plan.name, workout.workoutType)
            } else {
                logger.warn("No workout found for day {} in plan '{}'", dayNumber, plan.name)
            }
            workout
        } catch (e: Exception) {
            logger.error("Failed to retrieve today's workout for plan {}: {}", plan.id, e.message)
            throw e
        }
    }

    override suspend fun getTrainingDaysForPlan(planId: Long): List<TrainingDay> {
        return try {
            val trainingDays = trainingDayRepository.findByTrainingPlanId(planId)
            logger.info("Retrieved {} training days for plan: {}", trainingDays.size, planId)
            trainingDays
        } catch (e: Exception) {
            logger.error("Failed to retrieve training days for plan {}: {}", planId, e.message)
            throw e
        }
    }

    override suspend fun assessFitnessLevel(fitnessLevelRequest: FitnessLevelRequest): FitnessLevel {
        logger.info("Assessing fitness level for user: {}", fitnessLevelRequest.userId)

        return try {
            val fitnessLevel = geminiService.determineFitnessLevel(fitnessLevelRequest)
            logger.info("Fitness level assessed as {} for user: {}", fitnessLevel, fitnessLevelRequest.userId)
            fitnessLevel
        } catch (e: Exception) {
            logger.error("Failed to assess fitness level for user {}: {}", fitnessLevelRequest.userId, e.message)
            throw e
        }
    }

    override suspend fun generateTrainingPlan(trainingPlanRequest: TrainingPlanRequest): TrainingPlan {
        logger.info("Generating training plan for user: {}", trainingPlanRequest.userId)

        return try {
            val (trainingPlan, trainingDays) = geminiService.generateTrainingPlan(trainingPlanRequest)
            val savedTrainingPlan = trainingPlanRepository.save(trainingPlan)

            trainingDays.map { day ->
                day.copy(trainingPlanId = savedTrainingPlan.id)
            }.forEach { day ->
                trainingDayRepository.save(day)
            }

            logger.info("Training plan '{}' generated and saved with {} days for user: {}",
                savedTrainingPlan.name, trainingDays.size, trainingPlanRequest.userId)

            savedTrainingPlan
        } catch (e: Exception) {
            logger.error("Failed to generate training plan for user {}: {}", trainingPlanRequest.userId, e.message)
            throw e
        }
    }
}