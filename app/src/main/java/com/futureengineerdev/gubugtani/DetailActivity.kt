package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ActivityDetailBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.CommentViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity(), View.OnClickListener{

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var commentViewModel : CommentViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var imageScaleZoom = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val article = intent.getParcelableExtra<ArticlesWithImages>(EXTRA_DATA)
        setupView(article!!)
    }

    private fun setupView(article: ArticlesWithImages) {
        with(binding){
            val articleId = article.articles.id
            tvDetailJudul.text = article.articles.title
            tvDetailIsiArtikel.text = article.articles.content
            val firstImageDisplay = article.article_images
            val loadImageArticles = firstImageDisplay[0].image
            if (loadImageArticles == null) {
                Glide.with(this@DetailActivity)
                    .load("https://app.gubuktani.com/storage/public/gubuk-tani-logo.png")
                    .centerCrop()
                    .into(ivDetailGambar)
            }
            else{
                Glide.with(this@DetailActivity)
                    .load("https://app.gubuktani.com/storage/$loadImageArticles")
                    .centerCrop()
                    .into(ivDetailGambar)
            }
        }
        binding.btnBackDetail.setOnClickListener(this@DetailActivity)
        binding.btnLihatComment.setOnClickListener{
            val targetFragment = CommentFragment()
            val bundle = Bundle().apply {
                putInt(EXTRA_ID, article.articles.id)
            }
            targetFragment.arguments = bundle
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayoutComment, targetFragment)
            fragmentTransaction.commit()
            binding.btnLihatComment.visibility = View.GONE
            binding.btnTutupComment.visibility = View.VISIBLE
            swipeUp()
            binding.btnTutupComment.setOnClickListener {
                swipeDown()
                binding.btnLihatComment.visibility = View.VISIBLE
                binding.btnTutupComment.visibility = View.GONE
            }
        }
    }
    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_ID = "extra_id"
    }

    private fun swipeUp(){
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.frameLayoutComment.startAnimation(slideUp)
        slideUp.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                binding.frameLayoutComment.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }

    private fun swipeDown(){
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.frameLayoutComment.startAnimation(slideDown)
        slideDown.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.frameLayoutComment.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
    }


    override fun onClick(v: View?) {
        when(v){
            binding.btnBackDetail -> finish()
            binding.ivDetailGambar -> {
                imageScaleZoom = !imageScaleZoom
                binding.ivDetailGambar.scaleType =
                    if (imageScaleZoom) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
            }
        }
    }
}