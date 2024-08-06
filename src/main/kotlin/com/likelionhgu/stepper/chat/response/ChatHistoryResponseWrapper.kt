package com.likelionhgu.stepper.chat.response

import com.likelionhgu.stepper.chat.ChatRole
import com.likelionhgu.stepper.openai.assistant.message.MessageResponseWrapper

data class ChatHistoryResponseWrapper(val messages: List<ChatHistoryResponse>) {
    data class ChatHistoryResponse(val messageId: String, val role: ChatRole, val content: String)

    companion object {
        fun of(response: MessageResponseWrapper): ChatHistoryResponseWrapper {
            val messages = response.data.map { message ->
                ChatHistoryResponse(
                    messageId = message.id,
                    role = ChatRole.of(message.role),
                    content = message.content.first().text.value
                )
            }
            return ChatHistoryResponseWrapper(messages)
        }

        fun firstOf(response: MessageResponseWrapper): ChatHistoryResponse {
            with(response.data.first()) {
                return ChatHistoryResponse(
                    messageId = id,
                    role = ChatRole.of(role),
                    content = content.first().text.value
                )
            }
        }
    }
}