package com.likelionhgu.stepper.openai.completion.request

import com.likelionhgu.stepper.openai.SimpleMessage

data class CompletionRequest(
    val model: String,
    val messages: List<SimpleMessage>
) {
    companion object {
        fun of(model: String, instructions: String, chatHistory: List<SimpleMessage>): CompletionRequest {
            val instructionMessage = SimpleMessage(
                role = USER_ROLE,
                content = instructions
            )
            val messages = chatHistory.plus(instructionMessage)
            return CompletionRequest(model, messages)
        }

        private const val USER_ROLE = "user"
    }
}
