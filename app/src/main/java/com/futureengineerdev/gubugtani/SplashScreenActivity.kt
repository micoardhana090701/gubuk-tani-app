package com.futureengineerdev.gubugtani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var logo: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        logo = findViewById(R.id.logo)

        logo.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}