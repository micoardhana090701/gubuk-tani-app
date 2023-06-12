package com.futureengineerdev.gubugtani.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ItemHistoryBinding
import com.futureengineerdev.gubugtani.response.DataItemHistory
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private val historyList: List<DataItemHistory>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(history: DataItemHistory){
            with(binding){
                tvNamaPenyakitHistory.setText(history.result)
                tvConfidenceHistory.setText(history.confidence)
                tvTimeHistory.setText(getTimeAgo(history))
                val loadImageHistory = history.image
                if (loadImageHistory != null) {
                    Glide.with(itemView.context)
                        .load("https://app.gubuktani.com/storage/$loadImageHistory")
                        .centerCrop()
                        .into(ivUserComment)
                }else{
                    Glide.with(itemView.context)
                        .load(R.drawable.null_pict)
                        .centerCrop()
                        .into(ivUserComment)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val history = historyList[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun getTimeAgo(history: DataItemHistory): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = System.currentTimeMillis()
        val uploadTime = dateFormat.parse(history.createdAt)?.time ?: currentTime

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