package com.likelionhgu.stepper.openai

import com.likelionhgu.stepper.chat.ChatRole

data class SimpleMessage(val role: String, val content: String) {

    companion object {
        fun withDefaultRole(content: String): SimpleMessage {
            return SimpleMessage(ChatRole.USER.alias, content)
        }
    }
}
