package com.example.myapplication8

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log

class MyService : Service() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                Log.d("MyService", "Service is running...")
                handler?.postDelayed(this, 5000)
            }
        }
        handler?.post(runnable!!)
        return START_STICKY
    }

    override fun onDestroy() {
        handler?.removeCallbacks(runnable!!)
        Log.d("MyService", "Service is stopped")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
