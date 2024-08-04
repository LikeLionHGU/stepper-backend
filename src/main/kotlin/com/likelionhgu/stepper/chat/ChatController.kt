package com.likelionhgu.stepper.chat

import com.likelionhgu.stepper.chat.response.ChatHistoryResponseWrapper
import com.likelionhgu.stepper.chat.response.ChatResponse
import com.likelionhgu.stepper.chat.response.ChatSummaryResponse
import com.likelionhgu.stepper.goal.GoalService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    private val goalService: GoalService,
    private val chatService: ChatService
) {
    @PostMapping("/v1/goals/{goalId}/chats")
    fun createChat(@PathVariable goalId: Long): ResponseEntity<ChatResponse> {
        val goal = goalService.goalInfo(goalId)
        val responseBody = chatService.initChat(goal)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }

    @GetMapping("/v1/chats/{chatId}/history")
    fun getChatHistory(@PathVariable chatId: String): ChatHistoryResponseWrapper {
        return chatService.chatHistoryOf(chatId).let(ChatHistoryResponseWrapper.Companion::of)
    }

    @PostMapping("/v1/chats/{chatId}/summary")
    fun getChatSummary(@PathVariable chatId: String): ResponseEntity<ChatSummaryResponse> {
        val responseBody = chatService.generateSummaryOf(chatId)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody)
    }
}