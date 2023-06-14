package com.futureengineerdev.gubugtani

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.futureengineerdev.gubugtani.databinding.ActivityDonateBinding

class DonateActivity : AppCompatActivity() {

    private var _binding: ActivityDonateBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnDonateBack.setOnClickListener {
            finish()
        }
    }
}