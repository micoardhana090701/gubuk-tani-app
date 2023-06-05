package com.futureengineerdev.gubugtani

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.futureengineerdev.gubugtani.databinding.ActivityDiseaseResultBinding
import com.futureengineerdev.gubugtani.databinding.ActivityLoginBinding
import com.futureengineerdev.gubugtani.databinding.ActivityRegisterBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.User
import com.futureengineerdev.gubugtani.ui.camera.CameraViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.DiseaseResultViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody

class DiseaseResultActivity : AppCompatActivity() {

    private var _binding : ActivityDiseaseResultBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var diseaseResultViewModel: DiseaseResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDiseaseResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch{
            setupViewModel()
        }

    }

    private suspend fun setupViewModel(){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        diseaseResultViewModel = ViewModelProvider(this, viewModelFactory)[DiseaseResultViewModel::class.java]

        val accessToken = pref.getUserKey().first()


        setupView()
        diseaseResultViewModel.getResultDisease(access_token = "Bearer $accessToken")
        diseaseResultViewModel.getDisease.observe(this){
            if (it != null){
                binding.tvResultDisease.setText(it.data?.result?.detection?.result)
                binding.tvAkurasi.setText(it.data?.result?.detection?.confidence)
            }
            else{
                binding.tvResultDisease.setText("Terjadi Kesalahan")
                binding.tvAkurasi.setText("00.00")
            }
        }
    }

    private fun setupView() {
        binding.btnBackResult.setOnClickListener{
            finish()
        }
    }
    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}