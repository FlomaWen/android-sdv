package com.supdevinci.aieaie.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.aieaie.model.OpenAiMessageBody
import com.supdevinci.aieaie.model.request.BodyToSend
import com.supdevinci.aieaie.model.response.GeneratedAnswer
import com.supdevinci.aieaie.repository.OpenAiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OpenAiViewModel : ViewModel() {
    private val repository = OpenAiRepository()
    private val _openAiResponse = MutableStateFlow<GeneratedAnswer?>(null)

    val openAiResponse: StateFlow<GeneratedAnswer?> = _openAiResponse

    fun fetchMessages() {
        viewModelScope.launch {
            try {
                val bodyToSend = BodyToSend(messages = listOf(OpenAiMessageBody(role= "assistant", content= "test test")))
                _openAiResponse.value = repository.getChatFromOpenAi(bodyToSend)
                Log.e("Fetch Messages List : ", _openAiResponse.value.toString())
            } catch (e: Exception) {
                // Handle error
                Log.e("Fetch Contact List : ", e.message.toString())
            }
        }
    }
}