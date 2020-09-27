package com.example.karnaughmap.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * from user WHERE login = :key")
    fun getUser(key: String): User?
}