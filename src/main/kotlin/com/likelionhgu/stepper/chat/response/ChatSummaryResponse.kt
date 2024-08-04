package com.likelionhgu.stepper.chat.response

import com.likelionhgu.stepper.openai.completion.response.CompletionResponse

data class ChatSummaryResponse(val content: String) {

    companion object {
        fun of(response: CompletionResponse): ChatSummaryResponse {
            val content = response.choices.first().message.content
            return ChatSummaryResponse(content)
        }
    }
}
