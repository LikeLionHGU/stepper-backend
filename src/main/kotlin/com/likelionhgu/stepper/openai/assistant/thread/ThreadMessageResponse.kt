package com.likelionhgu.stepper.openai.assistant.thread

data class ThreadMessageResponse(val data: List<Message>) {
    data class Message(val role: String, val content: List<Content>) {
        data class Content(val type: String, val text: Text) {
            data class Text(val value: String)
        }
    }
}
