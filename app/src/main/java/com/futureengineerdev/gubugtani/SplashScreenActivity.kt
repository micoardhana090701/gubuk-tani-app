package com.futureengineerdev.gubugtani

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.databinding.ActivitySplashBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!
    private var mShouldFinish = false
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        Handler(Looper.getMainLooper()).postDelayed({
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    binding.logo,
                    "logoLogin"
                )
            authViewModel.getUserKey().observe(this){
                if (it.isNullOrEmpty()) {
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java), optionsCompat.toBundle())
                } else {
                    startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java), optionsCompat.toBundle())
                }
            }
            mShouldFinish = true
        }, DELAY)
    }

    override fun onStop() {
        super.onStop()
        if (mShouldFinish) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
    }
    companion object {
        const val DELAY = 5000L
    }
}