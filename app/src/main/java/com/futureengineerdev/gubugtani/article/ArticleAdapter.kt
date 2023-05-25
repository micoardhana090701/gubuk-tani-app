package com.futureengineerdev.gubugtani.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.database.Articles
import com.futureengineerdev.gubugtani.databinding.ItemArtikelBinding

class ArticleAdapter: PagingDataAdapter<Articles, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {
    inner class ArticleViewHolder(private val binding: ItemArtikelBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Articles) {
            with(binding) {
                tvJudulArtikel.text = article.title
                tvDeskripsiSingkatArtikel.text = article.content
                Glide.with(itemView.context)
                    .load("https://app.gubuktani.com/storage/public/gubuk-tani-logo.png")
                    .centerCrop()
                    .into(ivGambarArtikel)
            }
        }
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ArticleViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder = ArticleViewHolder(
        ItemArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Articles>() {
            override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}