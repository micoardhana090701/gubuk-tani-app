package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.article.ArticleAdapter
import com.futureengineerdev.gubugtani.article.ViewModelArticleFactory
import com.futureengineerdev.gubugtani.databinding.ActivityDiseaseResultBinding
import com.futureengineerdev.gubugtani.disease.DiseaseAdapter
import com.futureengineerdev.gubugtani.disease.DiseaseViewModel
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DataItem
import com.futureengineerdev.gubugtani.response.DataItemHistory
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DiseaseResultActivity : AppCompatActivity() {

    private var _binding : ActivityDiseaseResultBinding? = null
    private val binding get() = _binding!!
    private val articleAdapter = ArticleAdapter()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var comunityViewModel: ComunityViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDiseaseResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupRecyclerView()

    }

    private fun setupViewModel(){

        val resultDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_RESULT)
        val confidenceDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_CONFIDENCE)
        val imageDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_IMAGE)
        val description = intent.getStringExtra(DiseaseResultActivity.EXTRA_DESCRIPTION_DISEASE)
        binding.tvResultDisease.setText(resultDisease.toString())
        binding.tvAkurasi.setText(confidenceDisease.toString())

        if(description == null){
            binding.tvDeskripsiPenangananResult.setText("tidak ada data")
        } else{
            binding.tvDeskripsiPenangananResult.setText(description.toString())
        }
        
        Glide.with(this)
            .load("https://app.gubuktani.com/storage/$imageDisease")
            .centerCrop()
            .into(binding.imageView5)

        if (resultDisease.toString() == "Sehat"){
            binding.imageView4.setImageResource(R.drawable.outline_check_circle_24)
            binding.textView10.setText("Tanaman Anda")
            binding.textView12.visibility = View.GONE
        } else{
            binding.imageView4.setImageResource(R.drawable.outline_warning_amber_24)
            binding.textView10.setText("Penyakit Tanaman Anda")
        }


        binding.btnBackResult.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
        lifecycleScope.launch {
            articleItem()
        }
    }

    private fun articleItem() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        comunityViewModel = ViewModelProvider(
            this,
            ViewModelArticleFactory(this)
        )[ComunityViewModel::class.java]
        val resultDisease = intent.getStringExtra(DiseaseResultActivity.EXTRA_RESULT)
        val searchQuery = resultDisease.toString()
        comunityViewModel.getArticle(searchQuery).observe(this) {
            CoroutineScope(Dispatchers.IO).launch {
                articleAdapter.submitData(it)
            }
        }

    }
    private fun setupRecyclerView() {
        with(binding.rvPenangangan){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DiseaseResultActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = articleAdapter
        }
    }


    companion object{
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_DESCRIPTION_DISEASE = "extra_description_disease"
    }

}