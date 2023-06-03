package com.futureengineerdev.gubugtani.plant

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.DetailActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ItemChooseBinding
import com.futureengineerdev.gubugtani.response.PlantsItem
import com.futureengineerdev.gubugtani.ui.camera.CameraFragment

class PlantAdapter(private val plantList: List<PlantsItem>): RecyclerView.Adapter<PlantAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemChooseBinding):RecyclerView.ViewHolder(binding.root) {
        private val plantName: TextView = itemView.findViewById(R.id.tvNamaPlant)
        private val plantImage: ImageView = itemView.findViewById(R.id.ivPlant)

        fun bind(plant: PlantsItem){
            with(binding){
                plantName.text = plant.name.toString()
                val firstImageDisplay = plant.image
                val loadImageArticles = firstImageDisplay
                if (loadImageArticles != null) {
                    Glide.with(itemView.context)
                        .load("https://app.gubuktani.com/storage/$loadImageArticles")
                        .centerCrop()
                        .into(plantImage)
                }
                else{
                    Glide.with(itemView)
                        .load(R.drawable.null_pict)
                        .centerCrop()
                        .into(plantImage)
                }
                root.setOnClickListener{
                    val detailIntent = Intent(root.context, CameraFragment::class.java)
                    detailIntent.putExtra(CameraFragment.EXTRA_DATA, plant)
                    root.context.startActivity(detailIntent)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemChooseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = plantList[position]
        holder.bind(plant)
    }
}