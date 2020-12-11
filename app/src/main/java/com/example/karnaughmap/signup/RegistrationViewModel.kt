package com.example.karnaughmap.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.karnaughmap.KmapRepository
import com.example.karnaughmap.Utils
import com.example.karnaughmap.database.KmapDatabase.Companion.getDatabase
import com.example.karnaughmap.database.User

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    val login = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val isLoginValid = MutableLiveData<Boolean>()
    val isPasswordValid = MutableLiveData<Boolean>()
    val isUserExist = MutableLiveData<Boolean>()
    val eventUserAuthorize = MutableLiveData<Boolean>()
    private val userRepository = KmapRepository(getDatabase(application))

    init {
        initializeFields()
    }

    fun actionToLoginComplete() {
        initializeFields()
    }

    fun addNewUser() {
        val l = login.value!!.trim()
        val p = password.value!!.trim()
        if (!isFieldValid(l)) {
            isLoginValid.value = false
        } else if (!isFieldValid(p)) {
            isPasswordValid.value = false
        } else {
            if (userExist(l)) {
                isUserExist.value = true
            } else {
                val hash = Utils.hash(p)
                userRepository.insertUser(User(0, l, hash))
                actionToLogin()
            }
        }
    }

    private fun actionToLogin() {
        eventUserAuthorize.value = true
    }

    private fun isFieldValid(field: String): Boolean {
        return field.isNotEmpty()
    }

    private fun userExist(login: String): Boolean {
        val user = userRepository.getUser(login)
        return (user != null)
    }

    private fun initializeFields() {
        login.value = ""
        password.value = ""
        isLoginValid.value = true
        isPasswordValid.value = true
        isUserExist.value = false
        eventUserAuthorize.value = false
    }

}