package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.databinding.ActivityRegisterBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setupView()
    }

    private fun setupView() {
        with(binding){
            btnDaftar.setOnClickListener(this@RegisterActivity)
            btnBatal.setOnClickListener(this@RegisterActivity)
        }
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        authViewModel.authInfo.observe(this){
            when(it){
                is Resource.Success -> {
                    Toast.makeText(this, "akun berhasil di buat", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    showLoadingRegister(false)
                    finishAffinity()
                }
                is Resource.Loading -> showLoadingRegister(true)
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoadingRegister(false)
                }
            }
        }
    }

    private fun showLoadingRegister(isLoadingRegister: Boolean) {
        binding.isLoadingRegister.visibility = if (isLoadingRegister) View.VISIBLE else View.GONE
        binding.btnDaftar.isEnabled = !isLoadingRegister
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnDaftar ->{
                val email = binding.etEmailR.text.toString()
                val username = binding.etUsernameR.text.toString()
                val name = binding.etNamaUserR.text.toString()
                val password = binding.etPasswordR.text.toString()
                val repassword = binding.etRePaswordR.text.toString()
                if(
                    binding.etEmailR.error == null &&
                    binding.etUsernameR.error == null &&
                    binding.etNamaUserR.error == null &&
                    binding.etPasswordR.error == null &&
                    binding.etRePaswordR.error == null
                ) {
                    if(repassword == password){
                        authViewModel.registerUser(name, username, email, password)
                    }
                    else{
                        Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(this, "Check Your Input", Toast.LENGTH_SHORT).show()
                }
            }
            binding.btnBatal ->{
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
    }
}