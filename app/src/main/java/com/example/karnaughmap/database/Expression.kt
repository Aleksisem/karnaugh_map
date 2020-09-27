package com.example.karnaughmap.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = arrayOf("user_id"),
    childColumns = arrayOf("owner_id"),
    onDelete = ForeignKey.CASCADE
)])
data class Expression(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "owner_id") val owner_id: Int,
    @ColumnInfo(name = "expression_long") val expressionLong: String,
    @ColumnInfo(name = "expression_short") val expressionShort: String,
    @ColumnInfo(name = "map_uri") val uri: String
)