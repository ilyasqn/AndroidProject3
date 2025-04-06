package com.example.myapplication8

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.startServiceBtn)
        val stopBtn = findViewById<Button>(R.id.stopServiceBtn)

        startBtn.setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }

        stopBtn.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }
}
