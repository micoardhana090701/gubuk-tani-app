package com.futureengineerdev.gubugtani.article

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.futureengineerdev.gubugtani.DetailActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.database.ArticleImages
import com.futureengineerdev.gubugtani.database.Articles
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ItemArtikelBinding

class ArticleAdapter : PagingDataAdapter<ArticlesWithImages, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    inner class ArticleViewHolder(private val binding: ItemArtikelBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticlesWithImages) {
            with(binding) {
                tvJudulArtikel.text = article.articles.title
                tvDeskripsiSingkatArtikel.text = article.articles.content
                val firstImageDisplay = article.article_images
                val loadImageArticles = firstImageDisplay[0].image
                if (loadImageArticles != null) {
                    Glide.with(itemView.context)
                        .load("https://app.gubuktani.com/storage/$loadImageArticles")
                        .centerCrop()
                        .into(ivGambarArtikel)
                }
                else{
                    Glide.with(itemView.context)
                        .load(R.drawable.null_pict)
                        .centerCrop()
                        .into(ivGambarArtikel)
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