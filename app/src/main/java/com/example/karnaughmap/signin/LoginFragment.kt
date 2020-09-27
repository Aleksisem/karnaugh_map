package com.example.karnaughmap.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.karnaughmap.R
import com.example.karnaughmap.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(inflater, R.layout.login_fragment, container, false)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel

        loginViewModel.eventUserAuthorize.observe(viewLifecycleOwner, Observer { userId ->
            userId?.let {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMenuFragment(userId))
            }
        })

        loginViewModel.eventUserRegister.observe(viewLifecycleOwner, Observer {
            if (it) {
                Log.i("LoginFragment", "Go to registration fragment")
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
                loginViewModel.onSignUpComplete()
            }
        })

        loginViewModel.isDataCorrect.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.usernameEdit.error = "Неверный логин/пароль"
                binding.passwordEdit.error = "Неверный логин/пароль"
            }
        })

        return binding.root
    }
}