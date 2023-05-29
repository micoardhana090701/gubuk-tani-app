package com.futureengineerdev.gubugtani.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ItemArtikelBinding
import com.futureengineerdev.gubugtani.databinding.ItemArtikelHomeBinding

class HomeAdapter : PagingDataAdapter<ArticlesWithImages, HomeAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    inner class ArticleViewHolder(private val binding: ItemArtikelHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticlesWithImages) {
            with(binding) {
                tvJudulArtikelHome.text = article.articles.title
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
}