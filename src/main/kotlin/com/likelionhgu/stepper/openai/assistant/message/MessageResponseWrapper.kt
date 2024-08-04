package com.likelionhgu.stepper.openai.assistant.message

import com.likelionhgu.stepper.openai.SimpleMessage

data class MessageResponseWrapper(val data: List<MessageResponse>) {
    data class MessageResponse(val id: String, val role: String, val content: List<Content>) {
        data class Content(val type: String, val text: Text) {
            data class Text(val value: String)
        }
    }

    fun toSimpleMessage(): List<SimpleMessage> {
        return data.map { messageResponse ->
            SimpleMessage(
                role = messageResponse.role,
                content = messageResponse.content.first().text.value
            )
        }
    }
}
