package com.supdevinci.aieaie.model

import com.google.gson.annotations.SerializedName

data class OpenAiMessageBody(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

