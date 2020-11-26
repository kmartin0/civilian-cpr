package com.km.civilian.cpr.ui.messagesList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.km.civilian.cpr.base.BaseViewModel
import com.km.civilian.cpr.model.Message
import com.km.civilian.cpr.repository.IMessageRepository
import kotlinx.coroutines.launch

class MessageListViewModel @ViewModelInject constructor(
    private val messageRepository: IMessageRepository
) : BaseViewModel() {

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