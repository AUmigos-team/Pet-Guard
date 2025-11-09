package br.com.petguard.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "report")
data class Report (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "address")
    val address: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "latitude")
    val latitude: Double? = null,

    @ColumnInfo(name = "longitude")
    val longitude: Double? = null,

    @ColumnInfo(name = "photoPath")
    val photoPath: String? = null,

    @ColumnInfo(name = "videoPath")
    val videoPath: String? = null,

    @ColumnInfo(name = "status")
    val status: String = "PENDING",

    @ColumnInfo(name = "createdAt")
    val createdAt: LocalDateTime? = null,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: LocalDateTime? = null,

    @ColumnInfo(name = "createdBy")
    val createdBy: String? = null,

    @ColumnInfo(name = "completedBy")
    val completedBy: String? = null
)