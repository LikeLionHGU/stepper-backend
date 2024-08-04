package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.openai.ModelType


class AssistantProperties(
    val welcomeMessages: List<String>,
    val instructions: String,
    model: String
) {
    val modelType: ModelType = ModelType.of(model)
}
