package com.likelionhgu.stepper.openai.assistant.thread

data class ThreadCreationRequest(
    val messages: List<Message>
) {
    data class Message(
        val role: String,
        val content: String
    ) {
        companion object {
            fun withAssistant(content: String): Message {
                return Message(DEFAULT_ROLE, content)
            }
        }
    }

    companion object {
        private const val DEFAULT_ROLE = "assistant"
        private const val GOAL_TITLE = "{goalTitle}"

        fun withDefault(contents: List<String>, goalTitle: String): ThreadCreationRequest {
            val messages = contents.map { content ->
                val replacedContent = content.replace(GOAL_TITLE, goalTitle)
                Message.withAssistant(replacedContent)
            }
            return ThreadCreationRequest(messages)
        }
    }
}
