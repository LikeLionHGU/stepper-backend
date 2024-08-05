package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.openai.ModelType


class AssistantProperties(
    val instructions: String,
    val welcomeMessages: List<String>,
    model: String
) {
    val modelType: ModelType = ModelType.of(model)
}
