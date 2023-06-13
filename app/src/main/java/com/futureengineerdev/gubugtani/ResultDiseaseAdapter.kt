package com.futureengineerdev.gubugtani

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.databinding.ItemPenangananResultBinding
import com.futureengineerdev.gubugtani.databinding.ItemPenyakitBinding
import com.futureengineerdev.gubugtani.disease.DiseaseAdapter
import com.futureengineerdev.gubugtani.response.DataItem

class ResultDiseaseAdapter(private var diseaseList: List<DataItem>): RecyclerView.Adapter<ResultDiseaseAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemPenangananResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: DataItem) {
            with(binding){
                tvDiseaseName.setText(disease.name)
                tvDeskripsiDesease.setText(disease.description)
                val loadImageDisease = disease.image
                if (loadImageDisease != null){
                    Glide.with(itemView.context)
                        .load("https://app.gubuktani.com/storage/$loadImageDisease")
                        .centerCrop()
                        .into(ivDiseasePictResult)
                }else{
                    Glide.with(itemView.context)
                        .load(R.drawable.null_pict)
                        .centerCrop()
                        .into(ivDiseasePictResult)
                }
                root.setOnClickListener{
                    val detailIntent = Intent(root.context, DetailDisease::class.java)
                    detailIntent.putExtra(DetailDisease.EXTRA_DISEASE_DATA, disease)
                    root.context.startActivity(detailIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPenangananResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diseaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disease = diseaseList[position]
        holder.bind(disease)
    }
    fun submitList(list: List<DataItem>) {
        diseaseList = list
        notifyDataSetChanged()
    }

}