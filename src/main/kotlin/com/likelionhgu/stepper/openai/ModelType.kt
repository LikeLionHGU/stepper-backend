package com.likelionhgu.stepper.openai

import com.likelionhgu.stepper.exception.ModelNotSupportedException

enum class ModelType(val id: String) {
    GPT_4O("gpt-4o"),
    GPT_4O_MINI("gpt-4o-mini");

    companion object {
        fun of(model: String): ModelType {
            return entries.find { supportedModel ->
                val modelId = model.takeIf(String::isNotBlank) ?: GPT_4O_MINI.id
                supportedModel.id == modelId
            } ?: throw ModelNotSupportedException("Model $model is not supported")
        }
    }
}