package com.example.mobilearek.helper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class BaseApplication : Application() {

    companion object{
        const val CHANNEL_1_ID = "channel1"
    }

    override fun onCreate() {
        super.onCreate()
        createNotifikasi()
    }

    private fun createNotifikasi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel satu",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "Ini Channnel 1"

            val manager : NotificationManager? = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel1)
        }
    }
}