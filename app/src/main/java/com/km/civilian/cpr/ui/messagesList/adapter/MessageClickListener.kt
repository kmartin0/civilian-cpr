package com.km.civilian.cpr.ui.messagesList.adapter

import com.km.civilian.cpr.model.Message

interface MessageClickListener {
    fun onNavigationClick(message: Message)
    fun onStartDeleteActionMode(message: Message)
    fun onDelete(messages: List<Message>)
    fun onShare(message: Message)
    fun onCopy(message: Message)
}