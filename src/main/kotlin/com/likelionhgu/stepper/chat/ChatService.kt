package com.likelionhgu.stepper.chat

import com.likelionhgu.stepper.chat.response.ChatHistoryResponseWrapper
import com.likelionhgu.stepper.chat.response.ChatHistoryResponseWrapper.ChatHistoryResponse
import com.likelionhgu.stepper.chat.response.ChatResponse
import com.likelionhgu.stepper.chat.response.ChatSummaryResponse
import com.likelionhgu.stepper.exception.FailedAssistantException
import com.likelionhgu.stepper.exception.FailedCompletionException
import com.likelionhgu.stepper.exception.FailedMessageException
import com.likelionhgu.stepper.exception.FailedRunException
import com.likelionhgu.stepper.exception.FailedThreadException
import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.openai.OpenAiProperties
import com.likelionhgu.stepper.openai.assistant.AssistantService
import com.likelionhgu.stepper.openai.assistant.message.MessageResponseWrapper
import com.likelionhgu.stepper.openai.assistant.request.AssistantRequest
import com.likelionhgu.stepper.openai.assistant.run.RunRequest
import com.likelionhgu.stepper.openai.assistant.thread.ThreadCreationRequest
import com.likelionhgu.stepper.openai.completion.CompletionService
import com.likelionhgu.stepper.openai.completion.request.CompletionRequest
import com.likelionhgu.stepper.websocket.MessagePayload
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import retrofit2.Call
import retrofit2.Response

@Service
class ChatService(
    private val assistantService: AssistantService,
    private val completionService: CompletionService,
    private val openAiProperties: OpenAiProperties,
    private val redisTemplate: StringRedisTemplate
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

    /**
     * Retrieve chat history of the given chatId.
     *
     * The chat history consists of messages between the user and the assistant.
     *
     * @param chatId The chatId to retrieve chat history.
     * @return The chat history of the given chatId.
     */
    fun chatHistoryOf(chatId: String): MessageResponseWrapper {
        return assistantService.listMessagesOf(chatId).resolve()
            ?: throw FailedThreadException("Failed to get chat history for thread $chatId")
    }

    fun generateQuestion(chatId: String, message: MessagePayload): ChatHistoryResponse {
        addMessageToThread(chatId, message)
        runAssistantOn(chatId).also { runId ->
            waitUntilRunComplete(chatId, runId)
        }
        return assistantService.listMessagesOf(chatId).resolve()
            ?.let(ChatHistoryResponseWrapper.Companion::firstOf)
            ?: throw FailedMessageException("Failed to retrieve messages of thread $chatId")
    }

    private fun addMessageToThread(chatId: String, message: MessagePayload) {
        logger.info("Adding message to thread $chatId")
        assistantService.createMessageOf(chatId, message.toSimpleMessage()).resolve()
            ?: throw FailedMessageException("Failed to add message to thread $chatId")
    }

    private fun runAssistantOn(chatId: String): String {
        logger.info("Running assistant on thread $chatId")
        val run = assistantService.createRunOf(chatId, RunRequest(assistant())).resolve()
            ?: throw FailedRunException("Failed to run assistant on thread $chatId")
        return run.id
    }

    private fun waitUntilRunComplete(chatId: String, runId: String) {
        do {
            Thread.sleep(1_000)
            val run = assistantService.retrieveRunOf(chatId, runId).resolve()
                ?: throw FailedRunException("Failed to retrieve run $runId of thread $chatId")
        } while (run.status != "completed")
    }

    private fun assistant(assistantName: String = DEFAULT_ASSISTANT_NAME): String {
        val assistantKey = ASSISTANT_REDIS_KEY_PREFIX + assistantName
        return redisTemplate.opsForValue().get(assistantKey)
            ?: fetchAssistantOf(assistantName)
            ?: createAssistant(assistantName)
    }

    private fun fetchAssistantOf(assistantName: String): String? {
        val res = assistantService.listAssistants().resolve()
            ?: throw FailedAssistantException("Failed to fetch assistants")

        return res.data
            .find { it.name == assistantName }
            ?.let { assistant ->
                redisTemplate.opsForValue().set(ASSISTANT_REDIS_KEY_PREFIX + assistant.name, assistant.id)
                assistant.id
            }
    }

    private fun createAssistant(assistantName: String): String {
        with(openAiProperties.assistant) {
            val requestBody = AssistantRequest(modelType.id, instructions, assistantName)
            return assistantService.createAssistant(requestBody).resolve()
                ?.let {
                    redisTemplate.opsForValue().set(ASSISTANT_REDIS_KEY_PREFIX + assistantName, it.id)
                    it.id
                } ?: throw FailedAssistantException("Failed to create assistant")
        }
    }

    fun generateSummaryOf(chatId: String): ChatSummaryResponse {
        val chatHistory = chatHistoryOf(chatId).toSimpleMessage()

        with(openAiProperties.completion) {
            val requestBody = CompletionRequest.of(modelType.id, instructions, chatHistory)
            return completionService.createChatCompletion(requestBody).resolve()
                ?.let(ChatSummaryResponse.Companion::of)
                ?: throw FailedCompletionException("Failed to create completion for chat $chatId")
        }
    }

    companion object {
        private const val ASSISTANT_REDIS_KEY_PREFIX = "openai:assistant:"
        private const val DEFAULT_ASSISTANT_NAME = "default"
        private val logger = LoggerFactory.getLogger(ChatService::class.java)
    }
}

private fun <T> Call<T>.resolve(): T? {
    return execute().run(Response<T>::body)
}