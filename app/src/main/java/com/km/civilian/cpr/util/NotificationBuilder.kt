package com.km.civilian.cpr.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.km.civilian.cpr.R
import com.km.civilian.cpr.model.Message
import java.text.DateFormat

class NotificationBuilder {

    companion object {

        private const val CHANNEL_ID = "0"

        /**
         * Displays a notification for a [message]. Clicking on it will open google maps with
         * the navigation.
         */
        fun build(context: Context, message: Message) {
            // Pending intent for opening google maps with navigation to the end destination.
            val pendingMapsIntent =
                PendingIntent.getActivity(context, 1, MapsUtils.getMapsIntentForMessage(message), 0)

            // Builder obj with notification info.
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cpr)
                .setContentTitle(
                    context.getString(
                        R.string.notification_title,
                        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                            .format(message.date)
                    )
                )
//                .setContentTitle(
//                    "${context.getString(R.string.emergency)}: ${
//                        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
//                            .format(message.date)
//                    }"
//                )
                .setContentText(message.text)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message.text)
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingMapsIntent)
                .setAutoCancel(true)

            // Create the notification channel for this notification.
            createNotificationChannel(context)

            // Display the notification.
            NotificationManagerCompat.from(context).notify(0, builder.build())
        }


        /**
         * Create a notification channel to assign with a notification.
         */
        private fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: String = context.getString(R.string.civil_assistance)
                val descriptionText = context.getString(R.string.emergency)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        /**
         * Removes all notifications from this app.
         */
        fun cancelAllNotifications(context: Context) {
            NotificationManagerCompat.from(context).cancelAll()
        }
    }

}