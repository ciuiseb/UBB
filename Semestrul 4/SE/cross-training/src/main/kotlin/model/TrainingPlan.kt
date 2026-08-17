package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDate
import jakarta.persistence.*
import jakarta.persistence.Entity

@Entity
@Table(name = "TrainingPlans")
@Serializable
data class TrainingPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INTEGER")
    val id: Long = 0,
    @Column(name = "user_id", nullable = false, columnDefinition = "INTEGER")
    val userId: Long,
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "target_distance", nullable = false)
    val targetDistance: String,
    @Column(name = "preparation_weeks", nullable = false)
    val preparationWeeks: Int,
    @Transient
    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate = LocalDate.now(),
    @Transient
    @Column(name = "end_date", nullable = false)
    val endDate: LocalDate = startDate.plusDays(preparationWeeks * 7L)
)