package com.futureengineerdev.gubugtani

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.futureengineerdev.gubugtani.databinding.ActivityDonateBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.payment.PaymentMethodAdapter
import com.futureengineerdev.gubugtani.payment.PaymentMethodViewModel
import com.futureengineerdev.gubugtani.response.PaymentMethodsItem
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DonateActivity : AppCompatActivity() {

    private var _binding: ActivityDonateBinding? = null
    private val binding get() = _binding!!
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var paymentMethodViewModel: PaymentMethodViewModel
    private lateinit var methodAdapter: PaymentMethodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnDonateBack.setOnClickListener {
            finish()
        }
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        paymentMethodViewModel = ViewModelProvider(this, viewModelFactory)[PaymentMethodViewModel::class.java]
        lifecycleScope.launch{
            choosingMethod()
        }

    }

    private suspend fun choosingMethod() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        val accessToken = pref.getUserKey().first()

        paymentMethodViewModel.getPaymentMethod(access_token = "Bearer $accessToken")
        paymentMethodViewModel.method.observe(this){
            paymentAdapterInitialize(methodList = it.result.paymentMethods)
        }

    }

    private fun paymentAdapterInitialize(methodList: List<PaymentMethodsItem>) {
        methodAdapter = PaymentMethodAdapter(methodList)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding.rvPaymentMethod){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DonateActivity, LinearLayoutManager.VERTICAL, false)
            adapter = methodAdapter
        }
    }
}