package com.futureengineerdev.gubugtani.disease

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.DetailActivity
import com.futureengineerdev.gubugtani.DetailDisease
import com.futureengineerdev.gubugtani.DetailDisease.Companion.EXTRA_DISEASE_DATA
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ItemPenyakitBinding
import com.futureengineerdev.gubugtani.response.DataItem
import com.futureengineerdev.gubugtani.response.DiseaseResponse

class DiseaseAdapter (private val diseaseList: List<DataItem>): RecyclerView.Adapter<DiseaseAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemPenyakitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: DataItem) {
           with(binding){
               tvNamaPenyakitHome.setText(disease.name)
               val loadImageDisease = disease.image
               if (loadImageDisease != null){
                   Glide.with(itemView.context)
                       .load("https://app.gubuktani.com/storage/$loadImageDisease")
                       .centerCrop()
                       .into(ivGambarPenyakit)
               }else{
                     Glide.with(itemView.context)
                          .load(R.drawable.null_pict)
                          .centerCrop()
                          .into(ivGambarPenyakit)
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
        val binding = ItemPenyakitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diseaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disease = diseaseList[position]
        holder.bind(disease)
    }

}