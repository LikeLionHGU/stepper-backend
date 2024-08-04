package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.exception.ModelNotSupportedException

enum class ModelType(val id: String) {
    GPT_4O("gpt-4o"),
    GPT_4O_MINI("gpt-4o-mini");

    companion object {
        fun of(model: String): ModelType {
            require(model.isNotBlank()) { "Model must not be blank" }

            return entries.find { it.id == model }
                ?: throw ModelNotSupportedException("Model $model is not supported")
        }
    }
}