package com.km.civilian.cpr.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.km.civilian.cpr.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message ORDER BY date ASC")
    fun getAll(): Flow<List<Message>>

    @Insert
    suspend fun insert(message: Message)

    @Insert
    suspend fun insert(messages: List<Message>)

    @Delete
    suspend fun delete(message: Message)

    @Delete
    suspend fun delete(messages: List<Message>)

    @Query("DELETE FROM message")
    suspend fun deleteAll()

}