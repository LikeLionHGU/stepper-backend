package com.likelionhgu.stepper.chat

import com.likelionhgu.stepper.chat.response.ChatHistoryResponse
import com.likelionhgu.stepper.chat.response.ChatResponse
import com.likelionhgu.stepper.exception.FailedThreadException
import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.openai.OpenAiProperties
import com.likelionhgu.stepper.openai.assistant.AssistantService
import com.likelionhgu.stepper.openai.assistant.thread.ThreadCreationRequest
import org.springframework.stereotype.Service
import retrofit2.Call
import retrofit2.Response

@Service
class ChatService(
    private val assistantService: AssistantService,
    private val openAiProperties: OpenAiProperties
) {

    /**
     * Initialize a chat with the goal.
     *
     * @param goal The goal to initialize the chat with.
     * @return The thread ID of the chat.
     */
    fun initChat(goal: Goal): ChatResponse {
        val contents = openAiProperties.assistant.welcomeMessages
        val requestBody = ThreadCreationRequest.withDefault(contents, goal.title)

        return assistantService.createThread(requestBody).resolve()
            ?.let(ChatResponse.Companion::of)
            ?: throw FailedThreadException("Failed to create a thread")
    }

    fun chatHistoryOf(chatId: String): ChatHistoryResponse {
        return assistantService.messagesOfThread(chatId).resolve()
            ?.let(ChatHistoryResponse.Companion::of)
            ?: throw FailedThreadException("Failed to get chat history for thread $chatId")
    }
}

private fun <T> Call<T>.resolve(): T? {
    return execute().run(Response<T>::body)
}