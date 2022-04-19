package com.wiryadev.binarbattle.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wiryadev.binarbattle.databinding.FragmentRegisterBinding
import com.wiryadev.binarbattle.showSnackbar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRegister.setOnClickListener {
                viewModel.register(
                    email = etEmail.text.toString(),
                    username = etUsername.text.toString(),
                    password = etPassword.text.toString()
                )
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            if (response.success) {
                binding.root.showSnackbar(response.dataRegister.username)
                Thread.sleep(Snackbar.LENGTH_LONG.toLong())
                findNavController().navigateUp()
            }
            Log.d("Register", "onViewCreated: ${response.dataRegister}")
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.root.showSnackbar(it.message.toString())
                Log.d("Register", "onViewCreated: ${it.message}")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}