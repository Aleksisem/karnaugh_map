package com.example.karnaughmap.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.karnaughmap.KmapRepository
import com.example.karnaughmap.database.KmapDatabase.Companion.getDatabase
import com.example.karnaughmap.database.User

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val login = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val eventUserAuthorize = MutableLiveData<Int?>()
    val eventUserRegister = MutableLiveData<Boolean>()
    val isDataCorrect = MutableLiveData<Boolean>()
    private val userRepository: KmapRepository

    init {
        userRepository = KmapRepository((getDatabase(application)))
        login.value = ""
        password.value = ""
        eventUserAuthorize.value = null
        eventUserRegister.value = false
        isDataCorrect.value = true
    }

    /**
     * Идентифицировать пользователя
     */
    fun signIn() {
        val user = userRepository.getUser(login.value!!)
        if (user != null) {
            Log.i("LoginViewModel", "User exist")
        } else {
            Log.i("LoginViewModel", "NO USER")
        }
        if (user != null) {
            if (user.password == password.value!!) {
                authorizeUser(user)
            } else {
                // Ошибка (неверные данные)
                isDataCorrect.value = false
            }
        } else {
            // Ошибка (пользователя не существует)
            isDataCorrect.value = false
        }
    }

    /**
     * Зарегистрировать нового пользователя
     */
    fun signUp() {
        eventUserRegister.value = true
    }

    /**
     * Завершить переход к полю регистрации
     */
    fun onSignUpComplete() {
        eventUserRegister.value = false
    }

    /**
     * Завершить авторизацию пользователя
     */
    fun authorizeUserComplete() {
        eventUserAuthorize.value = null
    }
    /**
     * Авторизировать пользователя
     * @param {user} пользователь
     */
    private fun authorizeUser(user: User) {
        eventUserAuthorize.value = user.user_id
    }
}