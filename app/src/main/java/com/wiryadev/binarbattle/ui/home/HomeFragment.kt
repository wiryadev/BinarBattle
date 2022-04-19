package com.wiryadev.binarbattle.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.wiryadev.binarbattle.databinding.FragmentHomeBinding
import com.wiryadev.binarbattle.pref.SessionPreference
import com.wiryadev.binarbattle.pref.dataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            SessionPreference.getInstance(requireContext().dataStore)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (viewModel.token != user.token) {
                viewModel.token = user.token
                viewModel.auth(viewModel.token)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.authResponse.observe(viewLifecycleOwner) { response ->
            val user = response.commonData
            with(binding) {
                etUsername.setText(user.username)
                etEmail.setText(user.email)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}