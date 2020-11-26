package com.km.civilian.cpr.repository

import androidx.annotation.WorkerThread
import com.km.civilian.cpr.database.MessageDao
import com.km.civilian.cpr.model.Message
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {

    val allMessages: Flow<List<Message>> = messageDao.getAll()

    @WorkerThread
    suspend fun insert(message: Message) {
        messageDao.insert(message)
    }

    @WorkerThread
    suspend fun insert(messages: List<Message>) {
        messageDao.insert(messages)
    }

    @WorkerThread
    suspend fun delete(message: Message) {
        messageDao.delete(message)
    }

    @WorkerThread
    suspend fun delete(messages: List<Message>) {
        messageDao.delete(messages)
    }

    suspend fun deleteAll() {
        messageDao.deleteAll()
    }

}