package com.supdevinci.aieaie.model.request

import com.google.gson.annotations.SerializedName
import com.supdevinci.aieaie.model.OpenAiMessageBody

data class BodyToSend(
    @SerializedName("messages") val messages: List<OpenAiMessageBody>,
    @SerializedName("model") val model: String = "gpt-3.5-turbo"
)