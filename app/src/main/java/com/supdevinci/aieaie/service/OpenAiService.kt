package com.supdevinci.aieaie.service

import com.supdevinci.aieaie.model.request.BodyToSend
import com.supdevinci.aieaie.model.response.GeneratedAnswer
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiService {
    @POST("chat/completions")
    suspend fun getMessages(@Body post: BodyToSend): GeneratedAnswer
}