package com.example.weedwatch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val logoutButton = findViewById<Button>(R.id.btnLogout)
        val detectWeedButton = findViewById<Button>(R.id.btnDetectWeed)
        val settingButton = findViewById<Button>(R.id.btnSettings)

        settingButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        detectWeedButton.setOnClickListener {
            startActivity(Intent(this, WeedDetectionActivity::class.java))
        }
    }
}


