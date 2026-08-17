package lab.mobile.frontend.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "offline_requests")
data class OfflineRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val operationType: String,
    val bookJson: String
)

@Dao
interface OfflineRequestDao {
    @Insert
    suspend fun insert(request: OfflineRequest)

    @Query("SELECT * FROM offline_requests")
    suspend fun getAllRequests(): List<OfflineRequest>

    @Query("DELETE FROM offline_requests WHERE id = :id")
    suspend fun deleteRequest(id: Int)
}