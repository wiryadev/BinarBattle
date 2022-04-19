package com.wiryadev.binarbattle.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wiryadev.binarbattle.R
import com.wiryadev.binarbattle.databinding.FragmentLoginBinding
import com.wiryadev.binarbattle.pref.SessionPreference
import com.wiryadev.binarbattle.pref.UserSession
import com.wiryadev.binarbattle.pref.dataStore
import com.wiryadev.binarbattle.showSnackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            SessionPreference.getInstance(requireContext().dataStore)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            Log.d("SessionPref", "onViewCreated: $user")
            if (user.token.isNotBlank()) {
                try {
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeFragment
                    )
                } catch (e: Throwable) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        with(binding) {
            btnLogin.setOnClickListener {
                viewModel.login(
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString()
                )
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment
                )
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            if (response.success) {
                val user = response.dataLogin
                binding.root.showSnackbar(user.email)
                viewModel.saveUser(
                    UserSession(
                        username = user.username,
                        email = user.email,
                        token = user.token,
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}