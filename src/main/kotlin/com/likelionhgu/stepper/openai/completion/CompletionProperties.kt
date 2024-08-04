package com.likelionhgu.stepper.openai.completion

import com.likelionhgu.stepper.openai.ModelType

class CompletionProperties(
    val instructions: String,
    model: String
) {

    val modelType: ModelType = ModelType.of(model)
}
