package com.futureengineerdev.gubugtani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.futureengineerdev.gubugtani.databinding.ActivityChooseBinding
import com.futureengineerdev.gubugtani.ui.camera.CameraFragment

class ChooseActivity : AppCompatActivity() {

    private var _binding: ActivityChooseBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnBackChoose.setOnClickListener {
            finish()
        }
        binding.btnSementara.setOnClickListener{
            startActivity(Intent(this, CameraFragment::class.java))
        }
    }
}