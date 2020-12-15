package com.km.civilian.cpr.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import com.km.civilian.cpr.model.Message
import com.km.civilian.cpr.repository.IMessageRepository
import com.km.civilian.cpr.util.Constants
import com.km.civilian.cpr.util.NotificationBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MySmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var messageRepository: IMessageRepository

    /**
     * Listens for sms messages received.
     * Stores non empty messages in the database and send out a notification if the message type is known
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Get the SMS message.
        getTextFromHartstichtingSms(intent.extras)?.let { textMessage ->
            val message = Message(0, textMessage, Date())

            // Insert the message in the app database if it contains text.
            if (message.text.isNotBlank()) {
                GlobalScope.launch(Dispatchers.IO) {
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
    private fun getTextFromHartstichtingSms(extras: Bundle?): String? {
        if (extras == null || !extras.containsKey("format") || !extras.containsKey("pdus")) return ""

        val format = extras.getString("format")
        val pdus = extras.get("pdus") as Array<*>
        var txt = ""
        var phoneNumber: String? = ""

        for (pdu in pdus) {
            val smsMessage = getSmsMessage(pdu as ByteArray?, format)
            txt += smsMessage?.displayMessageBody
            phoneNumber = smsMessage?.originatingAddress
        }

        // Return the message if the sms has come from the hartstichting
        return if (phoneNumber.equals(Constants.HARTSTICHTING_PHONE_NUMBER)) txt else null
    }

    /**
     * Constructs a SmsMessage object from a pdu ByteArray
     *
     * @param pdu ByteArray of the pdu
     * @param format The format the pdu is described in.
     * @return SmsMessage created from [pdu].
     */
    private fun getSmsMessage(pdu: ByteArray?, format: String?): SmsMessage? {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> SmsMessage.createFromPdu(
                pdu,
                format
            )
            else -> SmsMessage.createFromPdu(pdu)
        }
    }
}
