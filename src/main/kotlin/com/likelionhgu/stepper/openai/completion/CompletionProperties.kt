package com.likelionhgu.stepper.openai.completion

import com.likelionhgu.stepper.openai.ModelType

class CompletionProperties(
    val instructions: String,
    private val model: String
) {

    val modelType: ModelType = ModelType.of(model)
}
