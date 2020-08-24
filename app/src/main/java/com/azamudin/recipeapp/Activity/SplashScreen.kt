package com.azamudin.recipeapp.Activity

import App.AppStart
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azamudin.recipeapp.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this, AppStart::class.java)
            startActivity(mainIntent)
            finish()
        }, 1500)
    }
}