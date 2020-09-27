package com.example.karnaughmap.menu

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.karnaughmap.KmapRepository
import com.example.karnaughmap.database.Expression
import com.example.karnaughmap.database.KmapDatabase.Companion.getDatabase

class MenuViewModel(application: Application, userId: Int) : AndroidViewModel(application) {

    val eventCreateNewMap = MutableLiveData<Boolean>()
    var expressions: LiveData<List<Expression>>


    private val expressionRepository = KmapRepository(getDatabase(application))

    init {
        expressions = expressionRepository.getAllExpression(userId)
        eventCreateNewMap.value = false
        Log.i("MenuViewModel", expressions.toString())
    }

    fun createNewMap() {
        eventCreateNewMap.value = true
    }

    fun createNewMapComplete() {
        eventCreateNewMap.value = false
    }
}