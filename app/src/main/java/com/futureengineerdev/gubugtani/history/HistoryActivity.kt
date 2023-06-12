package com.futureengineerdev.gubugtani.history

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ActivityHistoryBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DataItemHistory
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var _binding: ActivityHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        lifecycleScope.launch{
            getHistoryItem()
        }
    }


    private suspend fun getHistoryItem(){
        val userId = intent.getIntExtra(HistoryActivity.EXTRA_USER_ID, 0)
        val pref = UserPreferences.getInstance(this.dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        val accessToken = pref.getUserKey().first()
        historyViewModel = ViewModelProvider(this, viewModelFactory)[HistoryViewModel::class.java]
        historyViewModel.getHistory(accessToken = "Bearer $accessToken", user_id = userId)
        historyViewModel.history.observe(this) {
            historyAdapterInitialize(it.result?.data as List<DataItemHistory>)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun historyAdapterInitialize(history: List<DataItemHistory>){
        historyAdapter = HistoryAdapter(history)
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.VERTICAL, false)
            adapter = historyAdapter
        }
        historyAdapter.notifyDataSetChanged()
    }

    companion object{
        const val EXTRA_USER_ID = "extra_user_id"
    }
}