package com.futureengineerdev.gubugtani.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ItemCommentBinding
import com.futureengineerdev.gubugtani.response.DataItemComment

class CommentAdapter (private val commentList: List<DataItemComment>): RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(comment: DataItemComment){
            with(binding){
                tvNameComment.setText(comment.user?.name)
                tvComment.setText(comment.comment)
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
}