package br.com.petguard.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user WHERE logged = 1 LIMIT 1")
    suspend fun getLoggedUser(): User?

    @Query("DELETE FROM user")
    suspend fun cleanAll()

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE registration = :registration LIMIT 1")
    suspend fun findByRegistration(registration: String): User?

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
}