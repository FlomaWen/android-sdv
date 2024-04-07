package com.supdevinci.aieaie.repository

import com.supdevinci.aieaie.model.request.BodyToSend
import com.supdevinci.aieaie.model.response.GeneratedAnswer
import com.supdevinci.aieaie.service.RetrofitInstance

class OpenAiRepository {
        private val openAiService = RetrofitInstance.openAiService
        suspend fun getChatFromOpenAi(bodyToSend: BodyToSend): GeneratedAnswer {
                return openAiService.getMessages(bodyToSend)
        }
}


