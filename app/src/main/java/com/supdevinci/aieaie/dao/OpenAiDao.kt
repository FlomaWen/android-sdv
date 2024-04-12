package com.supdevinci.aieaie.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.supdevinci.aieaie.entity.ConversationEntity
import com.supdevinci.aieaie.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OpenAiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: Int): Flow<List<MessageEntity>>

    @Query("SELECT * FROM conversations")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Query("DELETE FROM conversations")
    suspend fun deleteAllConversations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createConversation(conversation: ConversationEntity): Long

    @Query("DELETE FROM conversations WHERE id = :conversationId")
    suspend fun deleteConversationById(conversationId: Int)

    @Query("SELECT * FROM conversations WHERE topic = :topic")
    fun getConversationsByTopic(topic: String): Flow<List<ConversationEntity>>
}