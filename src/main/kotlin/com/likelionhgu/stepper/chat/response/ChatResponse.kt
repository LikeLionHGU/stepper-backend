package com.likelionhgu.stepper.chat.response

import com.likelionhgu.stepper.openai.assistant.thread.ThreadResponse

data class ChatResponse(val chatId: String) {

    companion object {
        fun of(response: ThreadResponse): ChatResponse {
            return ChatResponse(response.id)
        }
    }
}
