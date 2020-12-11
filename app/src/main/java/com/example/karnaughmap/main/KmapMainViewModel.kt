package com.example.karnaughmap.main

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.karnaughmap.KmapRepository
import com.example.karnaughmap.database.Expression
import com.example.karnaughmap.database.KmapDatabase.Companion.getDatabase
import com.example.karnaughmap.Kmap

class KmapMainViewModel(application: Application, private val userId: Int) : AndroidViewModel(application) {

    val expressionLong = MutableLiveData<String>()
    val expressionShort = MutableLiveData<String>()
    val isExpressionLongValid = MutableLiveData<Boolean>()
    val eventMapSaved = MutableLiveData<Boolean>()
    val kmap = MutableLiveData<Kmap>()
    private val expressionRepository = KmapRepository(getDatabase(application))

    init {
        expressionLong.value = ""
        expressionShort.value = ""
        isExpressionLongValid.value = true
        eventMapSaved.value = false
        kmap.value = null
    }

    fun buildKmap() {
        val ex = expressionLong.value!!.replace("""\s+""".toRegex(), "").toLowerCase()
        if (ex.isEmpty()) {
            isExpressionLongValid.value = false
        } else {
            try {
                kmap.value = Kmap(ex)
            } catch (e: IllegalArgumentException) {
                isExpressionLongValid.value = false
            } catch (e: ArrayIndexOutOfBoundsException) {
                isExpressionLongValid.value = false
            }
        }
    }

    fun saveMap() {
        eventMapSaved.value = true
    }

    fun saveMapComplete(mapUri: Uri) {
        Log.i("KmapMainViewModel", "Map URI: ${mapUri.toString()}")
        val e = Expression(0, userId, expressionLong.value.toString(), expressionShort.value.toString(), mapUri.toString())
        Log.i("KmapMainViewModel", "New expression saved. UserID = $userId")
        expressionRepository.insertExpression(e)
        eventMapSaved.value = false
    }
}