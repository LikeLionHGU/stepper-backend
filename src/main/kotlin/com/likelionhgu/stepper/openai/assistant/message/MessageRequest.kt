package com.likelionhgu.stepper.openai.assistant.message

import com.likelionhgu.stepper.chat.ChatRole

data class MessageRequest(
    val role: String,
    val content: String
) {

    companion object {
        fun withDefaultRole(content: String): MessageRequest {
            return MessageRequest(ChatRole.USER.alias, content)
        }
    }
}
