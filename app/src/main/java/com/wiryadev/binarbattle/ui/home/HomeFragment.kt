package com.wiryadev.binarbattle.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.wiryadev.binarbattle.R
import com.wiryadev.binarbattle.databinding.FragmentHomeBinding
import com.wiryadev.binarbattle.pref.SessionPreference
import com.wiryadev.binarbattle.pref.dataStore
import com.wiryadev.binarbattle.showSnackbar
import com.wiryadev.binarbattle.uriToFile

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            SessionPreference.getInstance(requireContext().dataStore)
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val galleryFile = uriToFile(selectedImg, requireContext())
            viewModel.assignFile(galleryFile)
        }
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

        binding.ivPicture.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            val chooser = Intent.createChooser(
                intent, getString(R.string.gallery_chooser_title)
            )
            launcherGallery.launch(chooser)
        }



        viewModel.file.observe(viewLifecycleOwner) { file ->
            with(binding) {
                if (file != null) {
                    val bitmap = BitmapFactory.decodeFile(file.path)
                    ivPicture.load(bitmap)

                    binding.btnUpdate.setOnClickListener {
                        viewModel.upload(
                            token = viewModel.token,
                            username = etUsername.text.toString(),
                            email  = etEmail.text.toString(),
                            file = file,
                        )
                    }
                }
            }
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

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.root.showSnackbar("${it.message}")
            }
        }

        viewModel.updateResponse.observe(viewLifecycleOwner) { response ->
            Log.d("UpdateResponse", "onViewCreated: $response")
            val user = response.commonData
            with(binding) {
                etUsername.setText(user.username)
                etEmail.setText(user.email)


                if (response.success) {
                    root.showSnackbar("Update success")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}