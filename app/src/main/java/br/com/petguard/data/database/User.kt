package br.com.petguard.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "registration")
    val registration: String,

    @ColumnInfo(name = "cpf")
    val cpf: String,

    @ColumnInfo(name = "password")
    val password: String = "",

    @ColumnInfo(name = "logged")
    val logged: Boolean = false
)
