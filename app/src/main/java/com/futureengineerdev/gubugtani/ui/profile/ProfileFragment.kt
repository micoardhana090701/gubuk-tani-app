package com.futureengineerdev.gubugtani.ui.profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.datastore.core.DataStore
import android.view.ViewGroup
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.futureengineerdev.gubugtani.LoginActivity
import com.futureengineerdev.gubugtani.databinding.FragmentProfileBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.reflect.Constructor


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var  access_token: UserPreferences
    private lateinit var authViewModel : AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        setupView()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) { showProfile() }
    }

    private fun setupView() {
        binding.btnLogOut.setOnClickListener {
            authViewModel.logout()
        }
    }

    suspend fun showProfile(){
        val access_token = "Bearer ${access_token.getUserKey().first()}"
        profileViewModel.getProfile(access_token)
        profileViewModel.profileUser.observe(viewLifecycleOwner){
            if (it != null){
                binding.tvEmail.setText(it.result.user.email)
                binding.tvUsername.setText(it.result.user.username)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}