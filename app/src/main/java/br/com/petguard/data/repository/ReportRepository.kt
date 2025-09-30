package br.com.petguard.data.repository

import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.database.Report
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReportRepository(private val appDatabase: AppDatabase) {
    val pendingReports: Flow<List<Report>> = appDatabase.reportDao().getPendingReports()
    val completedReports: Flow<List<Report>> = appDatabase.reportDao().getCompletedReports()

    suspend fun saveReport(report: Report): Long {
        return withContext(Dispatchers.IO) {
            appDatabase.reportDao().saveReport(report)
        }
    }

    suspend fun markAsCompleted(id: Long) {
        withContext(Dispatchers.IO) {
            appDatabase.reportDao().markAsCompleted(id)
        }
    }

    suspend fun deleteById(id: Long) {
        withContext(Dispatchers.IO) {
            appDatabase.reportDao().deleteById(id)
        }
    }
}