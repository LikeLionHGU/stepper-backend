package com.likelionhgu.stepper.websocket

import com.likelionhgu.stepper.openai.SimpleMessage

data class MessagePayload(
    val content: String
) {

    fun toSimpleMessage(): SimpleMessage {
        return SimpleMessage.withDefaultRole(content)
    }
}