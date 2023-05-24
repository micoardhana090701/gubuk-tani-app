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
import com.futureengineerdev.gubugtani.LoginActivity
import com.futureengineerdev.gubugtani.databinding.FragmentProfileBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
//make UserProfile class
//make profile fragment

//undo this

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.btnLogOut.setOnClickListener {
            authViewModel.logout()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}