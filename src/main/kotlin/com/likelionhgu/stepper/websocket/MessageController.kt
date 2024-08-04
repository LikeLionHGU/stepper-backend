package com.likelionhgu.stepper.websocket

import com.likelionhgu.stepper.chat.ChatService
import com.likelionhgu.stepper.chat.response.ChatHistoryResponseWrapper
import com.likelionhgu.stepper.chat.response.ChatHistoryResponseWrapper.ChatHistoryResponse
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val chatService: ChatService,
) {

    /**
     * Retrieve chat history of the given chatId on initial subscription
     *
     * Triggered when client subscribes to: /app/chats/{chatId}/history
     *
     * @param chatId the chatId to retrieve chat history
     * @return the chat history of the given chatId
     */
    @SubscribeMapping("/chats/{chatId}/history")
    fun onSubscribe(@DestinationVariable chatId: String): ChatHistoryResponseWrapper {
        return chatService.chatHistoryOf(chatId).let(ChatHistoryResponseWrapper.Companion::of)
    }

    /**
     * Deliver incoming message to the assistant.
     * The message will be added to the chat history of the given chatId.
     *
     * Triggered when client sends message to: /app/chats/{chatId}/messages
     * Return the message to the client who subscribed to: /user/queue/messages
     *
     *
     * @param chatId the chatId where the message will be added
     * @param message generated message from the assistant
     */
    @MessageMapping("/chats/{chatId}/messages")
    @SendToUser("/queue/messages")
    fun onMessage(@DestinationVariable chatId: String, @Payload message: MessagePayload): ChatHistoryResponse {
        return chatService.generateQuestion(chatId, message)
    }
}