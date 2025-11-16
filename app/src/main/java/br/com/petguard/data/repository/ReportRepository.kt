package br.com.petguard.data.repository

import android.content.Context
import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.database.Report
import br.com.petguard.service.NotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ReportRepository(private val appDatabase: AppDatabase, private val context: Context) {
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

                if(it.reportedByUserId != null) {
                    val notificationService = NotificationService(context)
                    notificationService.sendNotification(updatedReport)
                }
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

    suspend fun getReportsByUserId(userId: String): List<Report> {
        return withContext(Dispatchers.IO) {
            val allPending = appDatabase.reportDao().getPendingReports().first()
            val allCompleted = appDatabase.reportDao().getCompletedReports().first()

            val allReports = (allPending + allCompleted).filter {
                it.reportedByUserId == userId
            }
            allReports
        }
    }

    suspend fun getUserPendingReportsCount(userId: String): Int {
        return withContext(Dispatchers.IO) {
            val allPending = appDatabase.reportDao().getPendingReports().first()
            allPending.count { it.reportedByUserId == userId }
        }
    }

    suspend fun getUserCompletedReportsCount(userId: String): Int {
        return withContext(Dispatchers.IO) {
            val allCompleted = appDatabase.reportDao().getCompletedReports().first()
            allCompleted.count { it.reportedByUserId == userId }
        }
    }
}