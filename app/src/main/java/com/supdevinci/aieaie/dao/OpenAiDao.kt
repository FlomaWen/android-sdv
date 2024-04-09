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
}