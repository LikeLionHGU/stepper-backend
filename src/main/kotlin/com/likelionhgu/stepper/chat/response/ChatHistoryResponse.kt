package com.likelionhgu.stepper.chat.response

import com.likelionhgu.stepper.openai.assistant.message.MessageResponseWrapper
import com.likelionhgu.stepper.chat.ChatRole

data class ChatHistoryResponse(val messages: List<Message>) {
    data class Message(val role: ChatRole, val content: List<String>)

    companion object {
        fun of(response: MessageResponseWrapper): ChatHistoryResponse {
            val messages = response.data.map { message ->
                Message(
                    role = ChatRole.of(message.role),
                    content = message.content.map { it.text.value }
                )
            }.reversed()
            return ChatHistoryResponse(messages)
        }
    }
}