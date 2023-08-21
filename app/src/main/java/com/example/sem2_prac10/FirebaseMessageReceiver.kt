package com.example.sem2_prac10

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessageReceiver: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if(message.getNotification() != null){
            showNotification(message.notification!!.title,
                    message.notification!!.body)
        }
    }

    fun getCustomDesign(title:String , message: String?):RemoteViews{

        val remoteviews = RemoteViews("package com.example.sem2_prac10",R.layout.activity_notification)
        remoteviews.setTextViewText(R.id.txtTitle,title)
        remoteviews.setTextViewText(R.id.txtMsg,message)
        remoteviews.setImageViewResource(R.id.icon,R.drawable.img)
        return remoteviews
    }


    private fun showNotification(title: String?, message: String?) {

        val channelId = "notification"
        val intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.img)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getCustomDesign(title!!,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId,"web_app",NotificationManager.IMPORTANCE_HIGH)

            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        notificationManager!!.notify(0,builder.build())
    }

}