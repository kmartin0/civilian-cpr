package com.km.civilian.cpr.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.km.civilian.cpr.R
import com.km.civilian.cpr.enum.MessageType
import java.util.*

@Entity
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val text: String,
    val date: Date
) {

    /**
     * Define message type based on the text.
     * @return MessageType of this message.
     */
    fun getType(): MessageType {
        return when {
            text.startsWith("REANIMATIE") -> MessageType.CPR
            text.startsWith("Haal AED") -> MessageType.AED

            else -> MessageType.UNKNOWN
        }
    }

    /**
     * Define drawable icon of this message based on the type.
     * @return Drawable res of this message.
     */
    @DrawableRes
    fun getTypeDrawableRes(): Int {
        return when (getType()) {
            MessageType.AED -> R.drawable.ic_defibrillator
            MessageType.CPR -> R.drawable.ic_cpr
            MessageType.UNKNOWN -> R.drawable.ic_baseline_chat_bubble_24
        }
    }
}