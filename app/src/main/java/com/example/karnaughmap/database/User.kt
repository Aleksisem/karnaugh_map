package com.example.karnaughmap.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id") var user_id: Int,
    @ColumnInfo(name = "login") var login: String,
    @ColumnInfo(name = "password") var password: String
)