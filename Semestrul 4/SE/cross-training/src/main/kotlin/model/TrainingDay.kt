package model

import jakarta.persistence.*
import jakarta.persistence.Entity
import jakarta.persistence.GenerationType.IDENTITY
import kotlinx.serialization.Serializable
import model.enums.WorkoutType

@Entity
@Table(name = "TrainingDays")
@Serializable
data class TrainingDay(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", columnDefinition = "INTEGER")
    val id: Long = 0,
    @Column(name = "training_plan_id", nullable = false, columnDefinition = "INTEGER")
    val trainingPlanId: Long,
    @Column(name = "day_number", nullable = false)
    val dayNumber: Int,
    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    val workoutType: WorkoutType,
    @Column(name = "distance")
    val distance: String? = null,
    @Column(name = "duration")
    val duration: String? = null,
    @Column(name = "description")
    val description: String
) {
    constructor() : this(0, 0, 0, WorkoutType.EASY_RUN, null, null, "")
}
