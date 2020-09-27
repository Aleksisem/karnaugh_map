package com.example.karnaughmap

import androidx.lifecycle.LiveData
import com.example.karnaughmap.database.*

class KmapRepository(private val database: KmapDatabase) {

    fun getAllExpression(userId: Int): LiveData<List<Expression>> {
        return database.expressionDao.getAllExpressions(userId)
    }

    fun insertExpression(expression: Expression) {
        database.expressionDao.insert(expression)
    }

    fun insertUser(user: User) {
        database.userDao.insert(user)
    }

    fun getUser(login: String): User? {
        return database.userDao.getUser(login)
    }
}