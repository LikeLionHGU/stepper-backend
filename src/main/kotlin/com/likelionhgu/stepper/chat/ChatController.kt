package com.likelionhgu.stepper.chat

import com.likelionhgu.stepper.chat.response.ChatHistoryResponseWrapper
import com.likelionhgu.stepper.chat.response.ChatResponse
import com.likelionhgu.stepper.chat.response.ChatSummaryResponse
import com.likelionhgu.stepper.goal.GoalService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val goalService: GoalService,
    private val chatService: ChatService
) {
    @PostMapping("/v1/goals/{goalId}/chat")
    fun createChat(@PathVariable goalId: Long): ChatResponse {
        val goal = goalService.goalInfo(goalId)
        return chatService.initChat(goal)
    }

    @GetMapping("/v1/chats/{chatId}/history")
    fun getChatHistory(@PathVariable chatId: String): ChatHistoryResponseWrapper {
        return chatService.chatHistoryOf(chatId).let(ChatHistoryResponseWrapper.Companion::of)
    }

    @PostMapping("/v1/chats/{chatId}/summary")
    fun getChatSummary(@PathVariable chatId: String): ChatSummaryResponse {
        return chatService.generateSummaryOf(chatId)
    }
}