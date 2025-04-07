package com.example.myapplication8

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var serviceIntent: Intent
    private val PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(this, MyService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
        )

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
                break
            }
        }
    }

    fun startService(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Требуется разрешение для работы сервиса", Toast.LENGTH_LONG).show()
                requestPermissions()
                return
            }
        }

        try {
            ContextCompat.startForegroundService(this, serviceIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка запуска сервиса: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun nextActivity(view: View) {
        startActivity(Intent(this, MainActivity2::class.java))
    }

    fun stopService(view: View) {
        try {
            stopService(serviceIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка остановки сервиса: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
