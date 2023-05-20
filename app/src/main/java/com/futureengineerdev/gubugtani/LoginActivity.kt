package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.databinding.ActivityLoginBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity(), View.OnClickListener {

//    private lateinit var btnLogin: Button
//    private lateinit var btnRegister: Button
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel
    private var mShouldFinish = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupView()

//        btnLogin = findViewById(R.id.btnLogin)
//        btnRegister = findViewById(R.id.btnRegister)
//
//        btnLogin.setOnClickListener{
//            startActivity(Intent(this, HomeActivity::class.java))
        }

    override fun onClick(v: View?) {
        when(v){
            binding.btnLogin->{
                if (canLogin()){
                    val email = binding.etEmail.text.toString()
                    val password = binding.etPassword.text.toString()
                    authViewModel.loginUser(email, password)
                }
                else{
                    Toast.makeText(this, "Check Your Input", Toast.LENGTH_SHORT).show()
                }
            }
            binding.btnRegister->{
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }

    }

    private fun canLogin() =
        binding.etEmail.error == null && binding.etEmail.error == null &&
                !binding.etEmail.text.isNullOrEmpty() && !binding.etEmail.text.isNullOrEmpty()

    private fun setupView() {
        with(binding) {
            btnLogin.setOnClickListener(this@LoginActivity)
            btnRegister.setOnClickListener(this@LoginActivity)
        }
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        authViewModel.authInfo.observe(this){
            when(it){
                is Resource.Success ->{
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                is Resource.Error ->{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

}