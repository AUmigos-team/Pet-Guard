package br.com.petguard.data.repository

import br.com.petguard.data.database.AppDatabase
import br.com.petguard.data.database.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(private val appDatabase: AppDatabase) {
    private val userDao = appDatabase.userDao()

    suspend fun getCurrentUser(): User? {
        return appDatabase.userDao().getLoggedUser()
    }

    suspend fun saveUser(user: User) {
        appDatabase.userDao().saveUser(user)
    }

    suspend fun updateUser(user: User) {
        appDatabase.userDao().update(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return appDatabase.userDao().findByEmail(email)
    }

    fun getCurrentUserType(): Flow<String> = flow {
        val user = getCurrentUser()
        emit(user?.userType ?: "COMMON")
    }

    suspend fun clearSession() {
        userDao.deleteAll()
    }

    suspend fun getLoggedUser(): User? {
        return userDao.getLoggedUser()
    }
}