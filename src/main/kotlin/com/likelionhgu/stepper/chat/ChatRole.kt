package com.likelionhgu.stepper.chat

import com.likelionhgu.stepper.exception.ChatRoleNotSupportedException

enum class ChatRole(
    val alias: String
) {
    USER("user"), CHATBOT("assistant");

    companion object {
        fun of(role: String): ChatRole = ChatRole.entries.find { it.alias == role }
            ?: throw ChatRoleNotSupportedException("Role $role is not supported")
    }
}