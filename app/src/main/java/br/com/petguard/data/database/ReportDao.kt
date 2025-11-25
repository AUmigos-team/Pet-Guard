package br.com.petguard.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReport(report: Report): Long

    @Query("SELECT * FROM report WHERE id = :id")
    suspend fun getById(id: Long): Report?

    @Query("SELECT * FROM report WHERE status = 'PENDING'")
    fun getPendingReports(): Flow<List<Report>>

    @Query("SELECT * FROM report WHERE status != 'PENDING' ORDER BY updatedAt DESC")
    fun getCompletedReports(): Flow<List<Report>>

//    @Query("UPDATE report SET status = 'COMPLETED' WHERE id = :id")
//    suspend fun markAsCompleted(id: Long)

    @Query("DELETE FROM report WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(report: Report)

    @Query("SELECT * FROM report WHERE status = 'PENDING' AND reportedByUserId = :userId")
    fun getPendingReportsByUserId(userId: String): Flow<List<Report>>

    @Query("SELECT * FROM report WHERE status != 'PENDING' AND reportedByUserId = :userId ORDER BY updatedAt DESC")
    fun getCompletedReportsByUserId(userId: String): Flow<List<Report>>

    @Query("SELECT COUNT(*) FROM report WHERE status = 'PENDING' AND reportedByUserId = :userId")
    suspend fun countPendingReportsByUserId(userId: String): Int

    @Query("SELECT COUNT(*) FROM report WHERE status != 'PENDING' AND reportedByUserId = :userId")
    suspend fun countCompletedReportsByUserId(userId: String): Int
}