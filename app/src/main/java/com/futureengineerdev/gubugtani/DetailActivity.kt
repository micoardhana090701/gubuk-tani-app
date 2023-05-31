package com.futureengineerdev.gubugtani

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), View.OnClickListener{

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
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

    }
    companion object {
        const val EXTRA_DATA = "extra_data"
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