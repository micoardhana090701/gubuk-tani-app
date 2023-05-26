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
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ItemArtikelBinding

class ArticleAdapter : PagingDataAdapter<ArticlesWithImages, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    inner class ArticleViewHolder(private val binding: ItemArtikelBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticlesWithImages) {
            with(binding) {
                tvJudulArtikel.text = article.articles.title
                tvDeskripsiSingkatArtikel.text = article.articles.content

                val loadImageArticles = if (article.articleImages.isNotEmpty()) {
                    article.articleImages[0].image
                } else {
                    R.drawable.null_pict
                }
                Glide.with(itemView.context)
                    .load(loadImageArticles)
                    .centerCrop()
                    .into(ivGambarArtikel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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