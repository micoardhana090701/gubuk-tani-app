package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ActivityDiseaseResultBinding
import com.futureengineerdev.gubugtani.databinding.ActivityLoginBinding
import com.futureengineerdev.gubugtani.databinding.ActivityRegisterBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.PlantDiseaseDetection
import com.futureengineerdev.gubugtani.response.PlantDiseaseResponse
import com.futureengineerdev.gubugtani.response.PlantsItem
import com.futureengineerdev.gubugtani.response.ResultPlantDisease
import com.futureengineerdev.gubugtani.response.User
import com.futureengineerdev.gubugtani.ui.camera.CameraFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDiseaseResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
    }

    private fun setupViewModel(){

        val resultDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_RESULT)
        val confidenceDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_CONFIDENCE)

        binding.tvResultDisease.setText(resultDisease.toString())
        binding.tvAkurasi.setText(confidenceDisease.toString())

        binding.btnBackResult.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }

    companion object{
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_CONFIDENCE = "extra_confidence"
    }

}