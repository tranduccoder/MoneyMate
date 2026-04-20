package com.example.moneymate.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.moneymate.R

object NotificationHelper {

    fun show(context: Context, title: String, message: String) {

        val notification = NotificationCompat.Builder(context, "money_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}