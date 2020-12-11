package com.example.karnaughmap.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.karnaughmap.R
import com.example.karnaughmap.databinding.RegistrationFragmentBinding

class RegistrationFragment : Fragment() {

    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<RegistrationFragmentBinding>(inflater, R.layout.registration_fragment, container, false)
        registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        binding.lifecycleOwner = this
        binding.registrationViewModel = registrationViewModel

        registrationViewModel.eventUserAuthorize.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
                registrationViewModel.actionToLoginComplete()
            }
        })

        registrationViewModel.isLoginValid.observe(viewLifecycleOwner, Observer { valid ->
            if (!valid) {
                binding.usernameEdit.error = "Некорректный логин"
            }
        })

        registrationViewModel.isPasswordValid.observe(viewLifecycleOwner, Observer { valid ->
            if (!valid) {
                binding.passwordEdit.error = "Некорректный пароль"
            }
        })

        registrationViewModel.isUserExist.observe(viewLifecycleOwner, Observer { exist ->
            if (exist) {
                binding.usernameEdit.error = "Данный логин уже занят"
            }
        })

        return binding.root
    }

}