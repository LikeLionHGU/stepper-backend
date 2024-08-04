package com.likelionhgu.stepper.websocket

import com.likelionhgu.stepper.openai.assistant.message.MessageRequest
import com.likelionhgu.stepper.openai.assistant.message.MessageResponseWrapper

data class MessagePayload(
    val content: String
) {

    fun toMessageRequest(): MessageRequest {
        return MessageRequest.withDefaultRole(content)
    }

    companion object {
        fun of(response: MessageResponseWrapper): MessagePayload {
            val recentMessage = response.firstContent()
            return MessagePayload(recentMessage)
        }
    }
}