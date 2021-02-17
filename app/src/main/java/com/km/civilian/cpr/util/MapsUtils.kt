package com.km.civilian.cpr.util

import android.content.Intent
import android.net.Uri
import com.km.civilian.cpr.enum.MessageType
import com.km.civilian.cpr.model.Message
import java.net.URLEncoder

class MapsUtils {
    companion object {
        /**
         * Create a google maps intent with navigation for the [message]
         *
         * @return Maps intent with location and navigation to the emergency.
         */
        fun getMapsIntentForMessage(message: Message): Intent {
            val gmmIntentUri: Uri = Uri.parse(parseMessageToMapsUri(message))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            return mapIntent
        }

        /**
         * Creates a google maps url which has the emergency location pre loaded with it's navigation
         * from the users' location.
         * @return String of the google maps uri.
         */
        fun parseMessageToMapsUri(message: Message): String? {
            // The base uri for google maps to a location.
            val baseMapsUri = "https://www.google.com/maps/dir/?api=1"
            return with(message) {
                when (getType()) {
                    // For CPR return a maps uri to the emergency destination.
                    MessageType.CPR -> {
                        val destination =
                            text.substringAfter("REANIMATIE! ").substringBefore(" https://")

                        "$baseMapsUri&destination=${
                            URLEncoder.encode(
                                destination,
                                "utf-8"
                            )
                        }"
                    }
                    // For AED return a maps uri to the AED as the first way point and then the emergency destination.
                    MessageType.AED -> {
                        val aedWayPoint =
                            text.substringAfter("Haal AED: ").substringBefore(", Pincode")
                        val destination =
                            text.substringAfter("Ga naar ").substringBefore(" https://")

                        "$baseMapsUri&destination=${
                            URLEncoder.encode(
                                destination,
                                "utf-8"
                            )
                        }&waypoints=${URLEncoder.encode(aedWayPoint, "utf-8")}"
                    }
                    // return null if the message is unknown.
                    MessageType.UNKNOWN -> null
                }
            }

        }
    }
}