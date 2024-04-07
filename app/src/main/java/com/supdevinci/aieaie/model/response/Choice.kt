package com.supdevinci.aieaie.model.response

import com.google.gson.annotations.SerializedName
import com.supdevinci.aieaie.model.OpenAiMessageBody

data class Choice(
    @SerializedName("message")
    val message: OpenAiMessageBody
)