package com.futureengineerdev.gubugtani

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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

            binding.btnPenanganan.visibility = View.GONE
            binding.btnTutupPenanganan.visibility = View.VISIBLE
            swipeUp()

            binding.btnTutupPenanganan.setOnClickListener {
                swipeDown()
                binding.btnPenanganan.visibility = View.VISIBLE
                binding.btnTutupPenanganan.visibility = View.GONE
            }
        }
    }

    private fun swipeUp(){
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.containerDetailDisease.startAnimation(slideUp)
        slideUp.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                binding.containerDetailDisease.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }

    private fun swipeDown(){
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.containerDetailDisease.startAnimation(slideDown)
        slideDown.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.containerDetailDisease.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }



    companion object {
        const val EXTRA_DISEASE_DATA = "extra_disease_data"
    }
}