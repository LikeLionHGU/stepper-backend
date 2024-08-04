package com.likelionhgu.stepper.openai.assistant.request

data class AssistantRequest(
    val model: String,
    val instructions: String,
    val name: String
) {
}
