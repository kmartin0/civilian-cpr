package com.km.civilian.cpr.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.km.civilian.cpr.R
import com.km.civilian.cpr.enum.MessageType
import com.km.civilian.cpr.model.Message
import com.km.civilian.cpr.ui.MainActivity
import java.text.DateFormat

class NotificationBuilder {

    companion object {

        private const val CHANNEL_ID = "civilian_cpr_channel_id"
        private const val NOTIFICATION_ID = 0

        /**
         * Displays a notification for a [message]. Clicking on it will open google maps with
         * the navigation.
         */
        fun build(context: Context, message: Message) {
            // Create the notification channel for this notification.
            createNotificationChannel(context)

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
                .setContentText(message.text)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message.text)
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(createPendingIntent(message, context))
                .setAutoCancel(true)

            // Display the notification.
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
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
         * Create a pending intent for a message type
         *
         * @param message The message used to determine the pending intent action
         * @param context
         * @return PendingIntent with action determined by message type.
         */
        private fun createPendingIntent(message: Message, context: Context): PendingIntent {
            return when (message.getType()) {
                MessageType.AED,
                MessageType.CPR -> {
                    // Return pending intent which starts google maps with directions.
                    PendingIntent.getActivity(
                        context,
                        1,
                        MapsUtils.getMapsIntentForMessage(message),
                        0
                    )
                }
                MessageType.UNKNOWN -> {
                    // Create intent which directs to the main activity.
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    // Return pending intent to start main activity.
                    PendingIntent.getActivity(context, 1, intent, 0)
                }
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