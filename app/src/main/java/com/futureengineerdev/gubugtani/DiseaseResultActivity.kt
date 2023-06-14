package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.databinding.ActivityDiseaseResultBinding
import com.futureengineerdev.gubugtani.disease.DiseaseAdapter
import com.futureengineerdev.gubugtani.disease.DiseaseViewModel
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DataItem
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DiseaseResultActivity : AppCompatActivity() {

    private var _binding : ActivityDiseaseResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var resultDiseaseAdapter: ResultDiseaseAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var diseaseViewModel: DiseaseViewModel
    private var filteredDisease: List<DataItem> = listOf()
    private var originalDisease: List<DataItem> = listOf()

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
        val imageDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_IMAGE)
        binding.tvResultDisease.setText(resultDisease.toString())
        binding.tvAkurasi.setText(confidenceDisease.toString())
        Glide.with(this)
            .load("https://app.gubuktani.com/storage/$imageDisease")
            .centerCrop()
            .into(binding.imageView5)

        if (resultDisease.toString() == "Sehat"){
            binding.imageView4.setImageResource(R.drawable.outline_check_circle_24)
            binding.textView10.setText("Tanaman Anda")
        } else{
            binding.imageView4.setImageResource(R.drawable.outline_warning_amber_24)
            binding.textView10.setText("Penyakit Tanaman Anda")
        }


        binding.btnBackResult.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
        lifecycleScope.launch {
            diseaseItem()
        }
    }
    private suspend fun diseaseItem(){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        val accessToken = pref.getUserKey().first()
        diseaseViewModel = ViewModelProvider(this, viewModelFactory)[DiseaseViewModel::class.java]

        diseaseViewModel.getDisease(access_token = "Bearer $accessToken")
        diseaseViewModel.disease.observe(this){
            val resultDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_RESULT)
            val resultDiseaseResource = resultDisease.toString()

            diseaseAdapterInitialize(it.result.data as List<DataItem>)
            filterDisease(resultDiseaseResource)
        }
    }
    private fun filterDisease(query: String) {
        val filteredList = originalDisease.filter { dataItem ->

            dataItem.name?.contains(query, ignoreCase = true) == true
        }
        filteredDisease = filteredList
        resultDiseaseAdapter.submitList(filteredDisease)
    }
    private fun diseaseAdapterInitialize(disease: List<DataItem>) {
        originalDisease = disease
        filteredDisease = disease
        resultDiseaseAdapter = ResultDiseaseAdapter(disease)
        binding.rvPenangangan.apply {
            layoutManager = LinearLayoutManager(this@DiseaseResultActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = resultDiseaseAdapter
        }
        resultDiseaseAdapter.submitList(filteredDisease)
    }

    companion object{
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_IMAGE = "extra_image"
    }

}