package com.km.civilian.cpr.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.km.civilian.cpr.model.Message
import com.km.civilian.cpr.repository.IMessageRepository
import com.km.civilian.cpr.util.Constants
import com.km.civilian.cpr.util.NotificationBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MySmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var messageRepository: IMessageRepository

    // Create a coroutine job to manage coroutines lifecycle in BroadcastReceiver
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    /**
     * Listens for sms messages received.
     * Stores non empty messages in the database and send out a notification if the message type is known
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Check that the intent action is the expected one (e.g., SMS_RECEIVED action)
        if (intent.action != "android.provider.Telephony.SMS_RECEIVED") {
            // If the action doesn't match, ignore the intent to avoid unwanted behavior
            return
        }

        // Get the SMS message.
        getTextFromHartstichtingSms(intent)?.let { textMessage ->
            val message = Message(0, textMessage, Date())

            // Insert the message in the app database if it contains text.
            if (message.text.isNotBlank()) {
                scope.launch {
                    messageRepository.insert(message)
                }
            }

            // Send the user a push notification for this message.
            NotificationBuilder.build(context, message)
        }
    }

    /**
     * Retrieve message from a hartstichting sms.
     *
     * @param extras Bundle containing the sms data.
     * @return String? text from the sms if it's from the hartstichting otherwise null.
     */
    private fun getTextFromHartstichtingSms(intent: Intent): String? {

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        val messageSB = StringBuilder()
        var phoneNumber: String? = null

        for (message in messages) {
            if (message.originatingAddress.equals(Constants.HARTSTICHTING_PHONE_NUMBER, ignoreCase = true)) {
                    messageSB.append(message.displayMessageBody)
                    phoneNumber = message.originatingAddress
                }
        }

        return if (phoneNumber?.equals(Constants.HARTSTICHTING_PHONE_NUMBER, ignoreCase = true) == true) messageSB.toString() else null
    }

}
