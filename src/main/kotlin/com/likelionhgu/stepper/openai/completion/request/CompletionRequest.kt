package com.likelionhgu.stepper.openai.completion.request

import com.likelionhgu.stepper.openai.SimpleMessage

data class CompletionRequest(
    val model: String,
    val messages: List<SimpleMessage>
) {
    companion object {
        fun of(model: String, instructions: String, chatHistory: List<SimpleMessage>): CompletionRequest {
            val systemMessage = SimpleMessage(
                role = SYSTEM_ROLE,
                content = instructions
            )
            val messages = listOf(systemMessage).plus(chatHistory)
            return CompletionRequest(model, messages)
        }

        private const val SYSTEM_ROLE = "system"
    }
}