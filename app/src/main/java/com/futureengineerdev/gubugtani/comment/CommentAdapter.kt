package com.futureengineerdev.gubugtani.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.databinding.ItemCommentBinding
import com.futureengineerdev.gubugtani.response.DataItemComment
import java.text.SimpleDateFormat
import java.util.Locale

class CommentAdapter (private val commentList: List<DataItemComment>): RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(comment: DataItemComment){
            with(binding){
                tvNameComment.setText(comment.user?.name)
                tvComment.setText(comment.comment)
                tvTimeComment.setText(getTimeAgo(comment))
                val loadImageComment = comment.user?.avatar
                if (loadImageComment != null) {
                    Glide.with(itemView.context)
                        .load("https://app.gubuktani.com/storage/$loadImageComment")
                        .centerCrop()
                        .into(ivUserComment)
                }else{
                    Glide.with(itemView.context)
                        .load(R.drawable.baseline_person_24)
                        .centerCrop()
                        .into(ivUserComment)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val comment = commentList[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
    fun getTimeAgo(comment: DataItemComment): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = System.currentTimeMillis()
        val uploadTime = dateFormat.parse(comment.createdAt)?.time ?: currentTime

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