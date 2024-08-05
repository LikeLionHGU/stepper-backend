package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.openai.ModelType


class AssistantProperties(
    val instructions: String,
    welcomeMessages: List<String>,
    model: String
) {
    val modelType: ModelType = ModelType.of(model)
    val welcomeMessages: List<String> = welcomeMessages.map {
        String(it.toByteArray(Charsets.ISO_8859_1))
    }
}
