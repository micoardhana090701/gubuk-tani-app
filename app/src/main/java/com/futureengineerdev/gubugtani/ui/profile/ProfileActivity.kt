package com.futureengineerdev.gubugtani.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.HistoryActivity
import com.futureengineerdev.gubugtani.LoginActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.UpdateActivity
import com.futureengineerdev.gubugtani.databinding.ActivityProfileBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        lifecycleScope.launch{
            setupViewModel()
        }
    }

    private suspend fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        val accessToken = pref.getUserKey().first()

        setupView()
        profileViewModel.getProfile(access_token = "Bearer $accessToken")
        profileViewModel.profileUser.observe(this){
            when (it) {
                is Resource.Success -> {
                    showLoading(false)
                    if (it != null){
                        binding.tvEmail.setText(it.data?.result?.user?.email)
                        binding.tvUsername.setText(it.data?.result?.user?.name)
                        val ivFotoProfile = binding.ivFotoProfil
                        if (it.data?.result?.user?.avatar == null){
                            Glide.with(this)
                                .load(R.drawable.baseline_account_circle_24)
                                .centerCrop()
                                .into(ivFotoProfile)
                        }
                        else{
                            Glide.with(this)
                                .load("https://app.gubuktani.com/storage/" + it.data.result.user.avatar)
                                .centerCrop()
                                .into(ivFotoProfile)
                        }
                    }
                    else{
                        val ivFotoProfile = binding.ivFotoProfil
                        binding.tvEmail.setText(it?.data?.result?.user?.email)
                        binding.tvUsername.setText(it?.data?.result?.user?.username)
                        Glide.with(this)
                            .load(R.drawable.baseline_account_circle_24)
                            .centerCrop()
                            .into(ivFotoProfile)
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                else -> {}
            }
        }
        binding.btnBackProfileUser.setOnClickListener {
            finish()
        }
        binding.btnHistory.setOnClickListener{
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.isLoadingProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setupView() {
        binding.btnLogOut.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
        binding.btnUbahProfile.setOnClickListener{
            startActivity(Intent(this, UpdateActivity::class.java))
        }
    }
}