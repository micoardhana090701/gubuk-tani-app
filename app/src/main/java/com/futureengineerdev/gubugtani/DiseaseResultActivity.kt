package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.futureengineerdev.gubugtani.databinding.ActivityDiseaseResultBinding

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

        if (resultDisease.toString() == "Healthy"){
            binding.imageView4.setImageResource(R.drawable.outline_check_circle_24)
            binding.textView10.setText("Tanaman Anda Sehat")
        } else{
            binding.imageView4.setImageResource(R.drawable.outline_warning_amber_24)
            binding.textView10.setText("Penyakit Tanaman Anda adalah")
        }


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