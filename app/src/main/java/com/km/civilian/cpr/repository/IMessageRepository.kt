package com.km.civilian.cpr.repository

import com.km.civilian.cpr.model.Message
import kotlinx.coroutines.flow.Flow

interface IMessageRepository {
    val allMessages: Flow<List<Message>>
    suspend fun insert(message: Message)
    suspend fun insert(messages: List<Message>)
    suspend fun delete(message: Message)
    suspend fun delete(messages: List<Message>)
    suspend fun deleteAll()
}