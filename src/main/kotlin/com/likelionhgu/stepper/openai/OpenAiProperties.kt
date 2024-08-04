package com.likelionhgu.stepper.openai

import com.likelionhgu.stepper.openai.assistant.AssistantProperties
import com.likelionhgu.stepper.openai.completion.CompletionProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = "custom.openai")
data class OpenAiProperties(
    val apiKey: String,

    @NestedConfigurationProperty
    val assistant: AssistantProperties,

    @NestedConfigurationProperty
    val completion: CompletionProperties
) {
}