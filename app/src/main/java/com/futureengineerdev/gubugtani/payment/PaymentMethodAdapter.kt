package com.futureengineerdev.gubugtani.payment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ItemChooseBinding
import com.futureengineerdev.gubugtani.databinding.ItemPaymentMethodBinding
import com.futureengineerdev.gubugtani.response.PaymentMethodsItem
import com.futureengineerdev.gubugtani.response.PlantsItem
import com.futureengineerdev.gubugtani.ui.camera.CameraFragment

class PaymentMethodAdapter (private val methodList: List<PaymentMethodsItem>): RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPaymentMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        private val methodName: TextView = itemView.findViewById(R.id.tvPaymentMethod)

        fun bind(method: PaymentMethodsItem) {
            with(binding) {
                methodName.text = method.method

                root.setOnClickListener {

                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return methodList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val method = methodList[position]
        holder.bind(method)
    }
}