package com.example.myapplication8

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MyService : Service() {
    private var toneGenerator: ToneGenerator? = null
    private val CHANNEL_ID = "channelId"
    private var notifManager: NotificationManager? = null
    private val TAG = "MyService"
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        try {
            Log.d(TAG, "onCreate: Создание сервиса")
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            Log.d(TAG, "onCreate: ToneGenerator создан успешно")
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: Ошибка", e)
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            Log.d(TAG, "onStartCommand: Запуск сервиса")
            createNotificationChannel()

            val sNotifBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle("Мой музыкальный плеер")
                .setContentText("Проигрывается звук")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val servNotification: Notification = sNotifBuilder.build()
            startForeground(1, servNotification)

            if (toneGenerator != null && !isPlaying) {
                Log.d(TAG, "onStartCommand: Начинаем воспроизведение")
                isPlaying = true
                Thread {
                    while (isPlaying) {
                        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)
                        try {
                            Thread.sleep(1100)
                        } catch (e: InterruptedException) {
                            break
                        }
                    }
                }.start()
            } else {
                Log.e(TAG, "onStartCommand: ToneGenerator не готов")
            }
        } catch (e: Exception) {
            Log.e(TAG, "onStartCommand: Ошибка", e)
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
            stopSelf()
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Канал сервиса"
            val channelDescription = "Музыкальный канал"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            channel.description = channelDescription

            notifManager = getSystemService(NotificationManager::class.java)
            notifManager?.createNotificationChannel(channel)
            Log.d(TAG, "createNotificationChannel: Канал создан")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Остановка сервиса")
        isPlaying = false
        toneGenerator?.release()
        toneGenerator = null
        Log.d(TAG, "onDestroy: ToneGenerator освобожден")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
