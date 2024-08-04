package com.likelionhgu.stepper.openai.assistant.message

data class MessageResponseWrapper(val data: List<MessageResponse>) {
    data class MessageResponse(val id: String, val role: String, val content: List<Content>) {
        data class Content(val type: String, val text: Text) {
            data class Text(val value: String)
        }
    }

    fun firstContent(): String {
        return data.firstOrNull()
            ?.content
            ?.firstOrNull()
            ?.text
            ?.value
            ?: ""
    }
}
