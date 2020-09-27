package com.example.karnaughmap.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpressionDao {

    /**
     * Добавить выражение в таблицу
     * @param {Expression} логическое выражение
     */
    @Insert
    fun insert(expression: Expression)

    /**
     * Получить все выражения, сохранённые определённым пользователем
     * @param {Int} ID пользователя
     * @return {LiveData<List<Expression>>} список выражений
     */
    @Query("SELECT * from expression WHERE owner_id = :userId")
    fun getAllExpressions(userId: Int): LiveData<List<Expression>>
}