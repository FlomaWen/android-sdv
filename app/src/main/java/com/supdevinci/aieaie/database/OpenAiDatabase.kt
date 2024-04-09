package com.supdevinci.aieaie.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.supdevinci.aieaie.dao.OpenAiDao
import com.supdevinci.aieaie.entity.ConversationEntity
import com.supdevinci.aieaie.entity.MessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ConversationEntity::class, MessageEntity::class], version = 2)
abstract class OpenAiDatabase : RoomDatabase() {
    abstract fun openAiDao(): OpenAiDao

    companion object {
        @Volatile
        private var INSTANCE: OpenAiDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): OpenAiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OpenAiDatabase::class.java,
                    "openAi_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        println("POPULATE DATA IN DB")
                        populateDatabase(database.openAiDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(openAiDao: OpenAiDao) {
            openAiDao.deleteAllMessages()
            openAiDao.deleteAllConversations()

        }
    }
}