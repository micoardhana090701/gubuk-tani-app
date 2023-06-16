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
import com.futureengineerdev.gubugtani.article.ArticleAdapter
import com.futureengineerdev.gubugtani.article.ViewModelArticleFactory
import com.futureengineerdev.gubugtani.databinding.ActivityDetailHistoryBinding
import com.futureengineerdev.gubugtani.disease.DiseaseViewModel
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DataItem
import com.futureengineerdev.gubugtani.response.DataItemHistory
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailHistoryActivity : AppCompatActivity() {

    private var _binding: ActivityDetailHistoryBinding? = null
    private val binding get() = _binding!!
    private val articleAdapter = ArticleAdapter()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var comunityViewModel: ComunityViewModel
    private lateinit var authViewModel: AuthViewModel



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
        setupRecyclerView()
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
            binding.tvPenangananHistory2.visibility = View.GONE
        } else{
            binding.imageView4.setImageResource(R.drawable.outline_warning_amber_24)
            binding.textView10.setText("Penyakit Tanaman Anda")
        }
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        comunityViewModel = ViewModelProvider(
            this,
            ViewModelArticleFactory(this)
        )[ComunityViewModel::class.java]
        val searchQuery = resultDisease.toString()
        comunityViewModel.getArticle(searchQuery).observe(this) {
            CoroutineScope(Dispatchers.IO).launch {
                articleAdapter.submitData(it)
            }
        }
    }
    private fun setupRecyclerView() {
        with(binding.rvPenanganganHistory){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailHistoryActivity, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
            adapter = articleAdapter
        }
    }

    companion object{
        const val EXTRA_DISEASE_RESULT = "extra_disease_result"
    }
}