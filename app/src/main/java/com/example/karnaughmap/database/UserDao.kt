package com.example.karnaughmap.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    /**
     * Получить пользователя по логину
     * @param {key} логин
     * @return объект пользователя
     */
    @Query("SELECT * from user WHERE login = :key")
    fun getUser(key: String): User?
}