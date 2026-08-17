package model

import jakarta.persistence.*
import jakarta.persistence.Entity
import model.enums.FitnessLevel
import model.enums.UserRole
import java.util.*

@Entity
@Table(name = "Users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INTEGER")
    val id: Long = 0,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false, unique = true, length = 50)
    val username: String,

    @Column(nullable = false, unique = true, length = 100)
    val email: String,

    @Column(nullable = false, length = 255)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "fitness_level", nullable = false)
    val fitnessLevel: FitnessLevel = FitnessLevel.NONE,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole = UserRole.USER,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    val createdAt: Date = Date()
)