package br.com.petguard.data.repository

import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.database.Report
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ReportRepository(private val appDatabase: AppDatabase) {
    val pendingReports: Flow<List<Report>> = appDatabase.reportDao().getPendingReports()
    val completedReports: Flow<List<Report>> = appDatabase.reportDao().getCompletedReports()

    suspend fun saveReport(report: Report): Long {
        return withContext(Dispatchers.IO) {
            appDatabase.reportDao().saveReport(report)
        }
    }

    suspend fun markAsCompleted(id: Long, completedBy: String) {
        withContext(Dispatchers.IO) {
            val report = appDatabase.reportDao().getById(id)
            report?.let {
                val updatedReport = it.copy(
                    status = "COMPLETED",
                    updatedAt = LocalDateTime.now(),
                    completedBy = completedBy
                )
                appDatabase.reportDao().update(updatedReport)
            }
        }
    }

    suspend fun deleteById(id: Long) {
        withContext(Dispatchers.IO) {
            appDatabase.reportDao().deleteById(id)
        }
    }

    suspend fun getReportById(id: Long): Report? {
        return withContext(Dispatchers.IO) {
            appDatabase.reportDao().getById(id)
        }
    }

    suspend fun updateReport(report: Report) {
        withContext(Dispatchers.IO) {
            appDatabase.reportDao().update(report)
        }
    }
}