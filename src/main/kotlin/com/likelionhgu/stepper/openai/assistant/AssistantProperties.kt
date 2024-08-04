package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.openai.ModelType


data class AssistantProperties(
    val welcomeMessages: List<String>,
    val instructions: String,
    private val model: String
) {
    val modelType: ModelType = ModelType.of(model)
}
