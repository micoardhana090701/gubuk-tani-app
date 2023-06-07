package com.futureengineerdev.gubugtani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.DialogPreference
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.databinding.ActivityDetailDiseaseBinding
import com.futureengineerdev.gubugtani.response.DataItem
import com.futureengineerdev.gubugtani.response.DiseaseResponse
import com.futureengineerdev.gubugtani.response.ResultDisease

class DetailDisease : AppCompatActivity() {

    private var _binding: ActivityDetailDiseaseBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val disease = intent.getParcelableExtra<DataItem>(EXTRA_DISEASE_DATA)
        setupView(disease!!)
    }
    private fun setupView(disease : DataItem){
        with(binding){
            tvNameDisease.text = disease.name
            tvDetailDisease.text = disease.description
            val imageDisplay = disease.image
            if (imageDisplay == null){
                Glide.with(this@DetailDisease)
                    .load(R.drawable.null_pict)
                    .centerCrop()
                    .into(ivDiseasePict)
            } else {
                Glide.with(this@DetailDisease)
                    .load("https://app.gubuktani.com/storage/$imageDisplay")
                    .centerCrop()
                    .into(ivDiseasePict)
            }
        }
        binding.btnBackDetailDisease.setOnClickListener{
            finish()
        }
        binding.btnPenanganan.setOnClickListener {
            val targetFragment = ArticleIncludedFragment()
            val bundle = Bundle().apply {
                putParcelable(ArticleIncludedFragment.EXTRA_ARTICLE, disease)
            }
            targetFragment.arguments = bundle
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container_detail_disease, targetFragment)
            fragmentTransaction.commit()

        }
    }



    companion object {
        const val EXTRA_DISEASE_DATA = "extra_disease_data"
    }
}