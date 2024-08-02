package com.likelionhgu.stepper.chat

import com.likelionhgu.stepper.exception.FailedThreadException
import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.openai.OpenAiProperties
import com.likelionhgu.stepper.openai.assistant.AssistantService
import com.likelionhgu.stepper.openai.assistant.thread.ThreadCreationRequest
import com.likelionhgu.stepper.openai.assistant.thread.ThreadResponse
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
    fun initChat(goal: Goal): String {
        val contents = openAiProperties.assistant.welcomeMessages
        val requestBody = ThreadCreationRequest.withDefault(contents, goal.title)

        return assistantService.createThread(requestBody).resolve()
            ?.let(ThreadResponse::id)
            ?: throw FailedThreadException("Failed to create a thread")
    }
}

private fun <T> Call<T>.resolve(): T? {
    return execute().run(Response<T>::body)
}