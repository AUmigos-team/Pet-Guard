package br.com.petguard.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "birth_date")
    val birthDate: String,

    @ColumnInfo(name = "registration")
    val registration: String? = null,

    @ColumnInfo(name = "cpf")
    val cpf: String,

    @ColumnInfo(name = "password")
    val password: String = "",

    @ColumnInfo(name = "user_type")
    val userType: String = "COMMON",

    @ColumnInfo(name = "logged")
    val logged: Boolean = false
)
