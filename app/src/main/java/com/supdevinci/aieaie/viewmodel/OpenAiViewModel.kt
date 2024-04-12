package com.supdevinci.aieaie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.aieaie.dao.OpenAiDao
import com.supdevinci.aieaie.entity.ConversationEntity
import com.supdevinci.aieaie.model.OpenAiMessageBody
import com.supdevinci.aieaie.model.request.BodyToSend
import com.supdevinci.aieaie.model.response.GeneratedAnswer
import com.supdevinci.aieaie.repository.OpenAiRepository
import kotlinx.coroutines.flow.Flow import kotlinx.coroutines.launch

class OpenAiViewModel(
    private val openAiDao: String,
    private val selectedSubject: String
) : ViewModel() {

    private val repository = OpenAiRepository()
    private val _openAiResponse = MutableLiveData<GeneratedAnswer?>()
    val openAiResponse: LiveData<GeneratedAnswer?> = _openAiResponse

    private val conversationHistory = mutableListOf<OpenAiMessageBody>()

    private val subject: String = selectedSubject

    init {
        if (conversationHistory.isEmpty()) {
            sendInitialMessage()
        }
    }

    private fun sendInitialMessage() {
        viewModelScope.launch {
            try {
                val initialMessage =
                    "Tu es un très gros consommateur de $selectedSubject et tu vas m'aider aujourd'hui, lors de ton premier message précise bien de quoi on va discuter"
                val bodyToSend = BodyToSend(messages = listOf(OpenAiMessageBody(role = "user", content = initialMessage)))
                _openAiResponse.value = repository.getChatFromOpenAi(bodyToSend)
                conversationHistory.add(OpenAiMessageBody(role = "user", content = initialMessage))
            } catch (e: Exception) {
                Log.e("Fetch Messages Error : ", e.message.toString())
            }
        }
    }

    fun fetchMessages() {
        viewModelScope.launch {
            try {
                val bodyToSend = BodyToSend(messages = conversationHistory.map { it.copy() })
                _openAiResponse.value = repository.getChatFromOpenAi(bodyToSend)
                Log.e("Fetch Messages List : ", _openAiResponse.value.toString())
            } catch (e: Exception) {
                Log.e("Fetch Messages Error : ", e.message.toString())
            }
        }
    }

    fun addMessage(message: OpenAiMessageBody) {
        conversationHistory.add(message)
    }

    fun addNewConversation(topic: String) {
        viewModelScope.launch {
            val conversationId = openAiDao.createConversation(ConversationEntity(topic = topic))
            // Optionally, you can do something with the conversationId
        }
    }

    fun deleteConversation(conversationId: Int) {
        viewModelScope.launch {
            openAiDao.deleteConversationById(conversationId)
        }
    }

    val conversationsByTopic: Flow<List<ConversationEntity>> = openAiDao.getConversationsByTopic(subject)
}
