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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.futureengineerdev.gubugtani.databinding.ActivityChooseBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.plant.PlantAdapter
import com.futureengineerdev.gubugtani.plant.PlantViewModel
import com.futureengineerdev.gubugtani.response.PlantsItem
import com.futureengineerdev.gubugtani.ui.camera.CameraFragment
import com.futureengineerdev.gubugtani.ui.profile.ProfileViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseActivity : AppCompatActivity() {

    private var _binding: ActivityChooseBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var plantViewModel: PlantViewModel
    private lateinit var plantAdapter : PlantAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnBackChoose.setOnClickListener {
            finish()
        }
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        plantViewModel = ViewModelProvider(this, viewModelFactory)[PlantViewModel::class.java]
        lifecycleScope.launch{
            choosingItem()
        }
    }

    private suspend fun choosingItem(){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        val accessToken = pref.getUserKey().first()

        plantViewModel.getPlant(access_token = "Bearer $accessToken")

        plantViewModel.plant.observe(this){
            plantAdapterInitialize(plantList = it.result.plants)
        }
    }

    private fun plantAdapterInitialize(plantList: List<PlantsItem>){
        plantAdapter = PlantAdapter(plantList)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding.rvChoose){
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = plantAdapter
        }
    }
}