package com.likelionhgu.stepper.openai.assistant.response

data class AssistantResponseWrapper(val data: List<AssistantResponse>) {
    data class AssistantResponse(
        val id: String,
        val name: String,
        val model: String,
        val instructions: String
    )
}
