    package com.supdevinci.aieaie.viewmodel

    import android.util.Log
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.supdevinci.aieaie.model.OpenAiMessageBody
    import com.supdevinci.aieaie.model.request.BodyToSend
    import com.supdevinci.aieaie.model.response.GeneratedAnswer
    import com.supdevinci.aieaie.repository.OpenAiRepository
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch

    class OpenAiViewModel(private val selectedSubject: String) : ViewModel() {
        private val repository = OpenAiRepository()
        private val _openAiResponse = MutableStateFlow<GeneratedAnswer?>(null)
        private val conversationHistory = mutableListOf<OpenAiMessageBody>()

        val openAiResponse: StateFlow<GeneratedAnswer?> = _openAiResponse

        init {
            if (conversationHistory.isEmpty()) {
                sendInitialMessage()
            }
        }

        private fun sendInitialMessage() {
            viewModelScope.launch {
                try {
                    val initialMessage = "Tu es un très gros consommateur de $selectedSubject et tu vas m'aider aujourd'hui, lors de ton premier message précise bien de quoi on va discuter"
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
    }
