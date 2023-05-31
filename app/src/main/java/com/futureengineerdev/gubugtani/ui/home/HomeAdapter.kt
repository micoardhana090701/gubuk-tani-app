package com.futureengineerdev.gubugtani.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.DetailActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ItemArtikelBinding
import com.futureengineerdev.gubugtani.databinding.ItemArtikelHomeBinding
import com.futureengineerdev.gubugtani.etc.timeStamp
import okhttp3.internal.http.toHttpDateOrNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeAdapter : PagingDataAdapter<ArticlesWithImages, HomeAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    inner class ArticleViewHolder(private val binding: ItemArtikelHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticlesWithImages) {
            with(binding) {
                tvJudulArtikelHome.text = article.articles.title
                tvTimeArtikelHome.text = getTimeAgo(article)
                val firstImageDisplay = article.article_images
                val loadImageArticles = firstImageDisplay[0].image
                if (loadImageArticles == "null") {
                    Glide.with(itemView.context)
                        .load(R.drawable.null_pict)
                        .centerCrop()
                        .into(ivGambarArtikelHome)
                }
                else{
                    Glide.with(itemView.context)
                        .load("https://app.gubuktani.com/storage/$loadImageArticles")
                        .centerCrop()
                        .into(ivGambarArtikelHome)
                }
                root.setOnClickListener{
                    val detailIntent = Intent(root.context, DetailActivity::class.java)
                    detailIntent.putExtra(DetailActivity.EXTRA_DATA, article)
                    root.context.startActivity(detailIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArtikelHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article)
        }
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesWithImages>() {
            override fun areItemsTheSame(oldItem: ArticlesWithImages, newItem: ArticlesWithImages): Boolean {
                return oldItem.articles.id == newItem.articles.id
            }

            override fun areContentsTheSame(oldItem: ArticlesWithImages, newItem: ArticlesWithImages): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun getTimeAgo(article: ArticlesWithImages): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = System.currentTimeMillis()
        val uploadTime = dateFormat.parse(article.articles.created_at)?.time ?: currentTime

        val timeDiff = currentTime - uploadTime
        val seconds = timeDiff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = months / 12

        return when {
            years > 0 -> "$years tahun yang lalu"
            months > 0 -> "$months bulan yang lalu"
            days > 0 -> "$days hari yang lalu"
            hours > 0 -> "$hours jam yang lalu"
            minutes > 0 -> "$minutes menit yang lalu"
            else -> "$seconds detik yang lalu"
        }
    }
}