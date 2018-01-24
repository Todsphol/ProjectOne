package th.co.todsphol.add.projectone.notification

import android.app.Notification
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import th.co.todsphol.add.projectone.R
import th.co.todsphol.add.projectone.activity.MapsActivity
import android.os.Vibrator




class MessagingReceiver : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        createNotification(remoteMessage)
    }

    private fun createNotification(remoteMessage: RemoteMessage) {
        val notificationPayload = remoteMessage.notification

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(this, MapsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)


        @Suppress("DEPRECATION")
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.map)
                .setContentTitle(notificationPayload?.title)
                .setContentText(notificationPayload?.body)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.colorGreen))
                .setLights(Color.BLUE, 1000, 300)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentInfo(notificationPayload?.title)
                .setVibrate(longArrayOf(1000, 3000, 1000, 3000, 1000))

//                .setDefaults(Notification.DEFAULT_VIBRATE)
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(123, notificationBuilder.build())
    }

}