package com.km.civilian.cpr.ui.messagesList

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.km.civilian.cpr.base.BaseViewModel
import com.km.civilian.cpr.database.AppDatabase
import com.km.civilian.cpr.model.Message
import com.km.civilian.cpr.repository.MessageRepository
import kotlinx.coroutines.launch

class MessageListViewModel(application: Application) : BaseViewModel(application) {

    private val db = AppDatabase.getDatabase(application.applicationContext)
    private val messageRepository = MessageRepository(db.messageDao())

    val messages: LiveData<List<Message>> = messageRepository.allMessages.asLiveData()

    /**
     * Insert [messages] in database
     */
    fun insertMessages(messages: List<Message>) {
        viewModelScope.launch {
            messageRepository.insert(messages)
        }
    }

    /**
     * Delete [messages] in database
     */
    fun deleteMessages(messages: List<Message>) {
        viewModelScope.launch {
            messageRepository.delete(messages)
        }
    }

    // adb emu sms send +3197005159109 REANIMATIE! Boris Pasternakstraat 57, Amsterdam https://hvw.page.link/2aNVw5ZrYS4GE8oB9

    // adb emu sms send +3197005159109 Haal AED: Daalwijkdreef 11, CODE Pincode 7229 Ga naar Anton de Komplein 232, Amsterdam https://hvw.page.link/ByHpTuTEdCivCCEu8

}