package com.futureengineerdev.gubugtani.history

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.DiseaseResultActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.ResultDiseaseAdapter
import com.futureengineerdev.gubugtani.databinding.ActivityDetailHistoryBinding
import com.futureengineerdev.gubugtani.disease.DiseaseViewModel
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DataItem
import com.futureengineerdev.gubugtani.response.DataItemHistory
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailHistoryActivity : AppCompatActivity() {

    private var _binding: ActivityDetailHistoryBinding? = null
    private val binding get() = _binding!!
    private var filteredDisease: List<DataItem> = listOf()
    private lateinit var resultDiseaseAdapter: ResultDiseaseAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var originalDisease: List<DataItem> = listOf()
    private lateinit var diseaseViewModel: DiseaseViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val history = intent.getParcelableExtra<DataItemHistory>(EXTRA_DISEASE_RESULT)

        supportActionBar?.hide()

        binding.btnBackResultHistory.setOnClickListener {
            finish()
        }
        setupViewModel(history!!)
    }
    private fun setupViewModel(history: DataItemHistory){
        val resultDisease = history.result
        val confidence = history.confidence
        val image = history.image
        binding.tvResultHistory.setText(resultDisease.toString())
        binding.tvAkurasiHistory.setText(confidence.toString())
        Glide.with(this)
            .load("https://app.gubuktani.com/storage/$image")
            .centerCrop()
            .into(binding.ivGambarHistory)
        if (resultDisease.toString() == "Sehat"){
            binding.imageView4.setImageResource(R.drawable.outline_check_circle_24)
            binding.textView10.setText("Tanaman Anda")
            binding.textView12.visibility = View.GONE
        } else{
            binding.imageView4.setImageResource(R.drawable.outline_warning_amber_24)
            binding.textView10.setText("Penyakit Tanaman Anda")
        }
        val history = intent.getParcelableExtra<DataItemHistory>(EXTRA_DISEASE_RESULT)
        lifecycleScope.launch {
            diseaseItem(history!!)
        }
    }
    private suspend fun diseaseItem(history: DataItemHistory){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        val accessToken = pref.getUserKey().first()
        diseaseViewModel = ViewModelProvider(this, viewModelFactory)[DiseaseViewModel::class.java]

        diseaseViewModel.getDisease(access_token = "Bearer $accessToken")
        diseaseViewModel.disease.observe(this){
            val resultDisease = history.result
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
        binding.rvPenanganganHistory.apply {
            layoutManager = LinearLayoutManager(this@DetailHistoryActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = resultDiseaseAdapter
        }
        resultDiseaseAdapter.submitList(filteredDisease)
    }

    companion object{
        const val EXTRA_DISEASE_RESULT = "extra_disease_result"
    }
}