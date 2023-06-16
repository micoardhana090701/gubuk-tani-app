package com.futureengineerdev.gubugtani.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.HomeActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ActivityDiseaseResultBinding
import com.futureengineerdev.gubugtani.databinding.ActivityPaymentResultBinding

class PaymentResultActivity : AppCompatActivity() {

    private var _binding : ActivityPaymentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBackPaymentResult.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        setupViewModel()

    }

    private fun setupViewModel(){
        val resultPayment = intent.getStringExtra(PaymentResultActivity.EXTRA_STATUS_PAYMENT)
        val imagePayment = intent.getStringExtra(PaymentResultActivity.EXTRA_IMAGE_PAYMENT)
        binding.tvStatusPayment.setText(resultPayment.toString())
        Glide.with(this)
            .load("https://app.gubuktani.com/storage/$imagePayment")
            .centerCrop()
            .into(binding.ivBuktiPembayaranResult)
    }

    companion object{
        const val EXTRA_IMAGE_PAYMENT = "extra_image_payment"
        const val EXTRA_STATUS_PAYMENT = "extra_status_payment"
    }
}