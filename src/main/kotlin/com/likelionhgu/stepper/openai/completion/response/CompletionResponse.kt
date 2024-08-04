package com.likelionhgu.stepper.openai.completion.response

import com.likelionhgu.stepper.openai.SimpleMessage

data class CompletionResponse(val id: String, val choices: List<Choice>) {
    data class Choice(val index: Int, val message: SimpleMessage)
}
