package com.likelionhgu.stepper.chat.response

import com.likelionhgu.stepper.exception.ChatRoleNotSupportedException
import com.likelionhgu.stepper.openai.assistant.thread.ThreadMessageResponse

data class ChatHistoryResponse(val messages: List<Message>) {
    data class Message(val role: ChatRole, val content: List<String>)

    companion object {
        fun of(response: ThreadMessageResponse): ChatHistoryResponse {
            val messages = response.data.map { message ->
                Message(
                    role = ChatRole.of(message.role),
                    content = message.content.map { it.text.value }
                )
            }.reversed()
            return ChatHistoryResponse(messages)
        }
    }

    enum class ChatRole(
        private val alias: String
    ) {
        USER("user"), CHATBOT("assistant");

        companion object {
            fun of(role: String): ChatRole = ChatRole.entries.find { it.alias == role }
                ?: throw ChatRoleNotSupportedException("Role $role is not supported")
        }
    }
}