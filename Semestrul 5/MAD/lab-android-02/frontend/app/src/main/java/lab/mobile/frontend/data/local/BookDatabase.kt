package lab.mobile.frontend.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lab.mobile.frontend.domain.model.Book

@Database(entities = [Book::class, OfflineRequest::class],
    version = 2,
    exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun offlineRequestDao(): OfflineRequestDao

    companion object {
        @Volatile
        private var Instance: BookDatabase? = null
        fun getDatabase(context: Context): BookDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    BookDatabase::class.java,
                    "book_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}