package br.com.petguard.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReport(report: Report): Long

    @Query("SELECT * FROM report WHERE id = :id")
    suspend fun getById(id: Long): Report?

    @Query("SELECT * FROM report WHERE status = 'PENDENTE'")
    fun getPendingDenuncias(): Flow<List<Report>>

    @Query("UPDATE report SET status = 'COMPLETED' WHERE id = :id")
    suspend fun markAsCompleted(id: Long)

    @Query("DELETE FROM report WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(report: Report)

}