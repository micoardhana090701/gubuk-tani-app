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
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.LoginActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.UpdateActivity
import com.futureengineerdev.gubugtani.article.ViewModelArticleFactory
import com.futureengineerdev.gubugtani.databinding.FragmentProfileBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.reflect.Constructor


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch{
            setupViewModel(view)
        }

    }

    private suspend fun setupViewModel(view: View) {
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        val accessToken = pref.getUserKey().first()

        setupView()
        profileViewModel.getProfile(access_token = "Bearer $accessToken")
        profileViewModel.profileUser.observe(viewLifecycleOwner){
            if (it != null){
                binding.tvEmail.setText(it.result.user.email)
                binding.tvUsername.setText(it.result.user.username)
                val ivFotoProfile = binding.ivFotoProfil
                if (it.result.user.avatar == null){
                    Glide.with(view.context)
                        .load(R.drawable.baseline_account_circle_24)
                        .centerCrop()
                        .into(ivFotoProfile)
                }
                else{
                    Glide.with(view.context)
                        .load("https://app.gubuktani.com/storage/" + it.result.user.avatar)
                        .centerCrop()
                        .into(ivFotoProfile)
                }
            }
            else{
                val ivFotoProfile = binding.ivFotoProfil
                binding.tvEmail.setText(it?.result?.user?.email)
                binding.tvUsername.setText(it?.result?.user?.username)
                Glide.with(view.context)
                    .load(R.drawable.baseline_account_circle_24)
                    .centerCrop()
                    .into(ivFotoProfile)
            }
        }
    }

    private fun setupView() {
        binding.btnLogOut.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.btnUbahProfile.setOnClickListener{
            startActivity(Intent(requireContext(), UpdateActivity::class.java))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}