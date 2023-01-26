package com.km.civilian.cpr.util

import com.km.civilian.cpr.model.Message
import java.util.*

class MockData {
    companion object {
        fun createMessages(): List<Message> {
            return listOf(
                Message(
                    0,
                    "REANIMATIE! A. Moenstraat 5, Amsterdam https://hvw.page.link/2aNVw5ZrYS4GE8oB9",
                    Date(1573518190000)
                ),
                Message(
                    0,
                    "REANIMATIE! Scheerderslaan 29, Wezep https://hvw.page.link/2aNVw5ZrYS4GE8oB9",
                    Date(1581757868000)
                ),
                Message(
                    0,
                    "Haal AED: Bronland 103, Pincode 8119 Ga naar Bornsesteeg 10, Wageningen https://hvw.page.link/ByHpTuTEdCivCCEu8",
                    Date(1590763814000)
                ),
                Message(
                    0,
                    "REANIMATIE! Mariëndaal 810, Ede https://hvw.page.link/2aNVw5ZrYS4GE8oB9",
                    Date(1596286272000)
                ),
                Message(
                    0,
                    "U heeft (1) voicemail ontvangen.",
                    Date(1596286272000)
                ),
                Message(
                    0,
                    "Haal AED: Maasstraat 47, Pincode 7229 Ga naar Volkerakstraat 57F, Amsterdam https://hvw.page.link/ByHpTuTEdCivCCEu8",
                    Date(1605440316000)
                )
            )
        }
    }
}

// adb emu sms send +3197005159109 REANIMATIE! A. Moenstraat 5, Amsterdam https://hvw.page.link/2aNVw5ZrYS4GE8oB9

// adb emu sms send +3197005159109 REANIMATIE! Scheerderslaan 29, Wezep https://hvw.page.link/2aNVw5ZrYS4GE8oB9

// adb emu sms send +3197005159109 U heeft (1) voicemail ontvangen.

// adb emu sms send +3197005159109 Haal AED: Bronland 103, Pincode 8119 Ga naar Bornsesteeg 10, Wageningen https://hvw.page.link/ByHpTuTEdCivCCEu8

// adb emu sms send +3197005159109 REANIMATIE! Mariëndaal 810, Ede https://hvw.page.link/2aNVw5ZrYS4GE8oB9

// adb emu sms send +3197005159109 Haal AED: Maasstraat 47, Pincode 7229 Ga naar Volkerakstraat 57F, Amsterdam https://hvw.page.link/ByHpTuTEdCivCCEu8