package com.km.civilian.cpr.ui.messagesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.km.civilian.cpr.base.BaseViewModel
import com.km.civilian.cpr.model.Message
import com.km.civilian.cpr.repository.IMessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor(
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
}