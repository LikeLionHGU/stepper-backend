package com.likelionhgu.stepper.chat

import com.likelionhgu.stepper.chat.response.ChatResponse
import com.likelionhgu.stepper.goal.GoalService
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
        val chatId = chatService.initChat(goal)

        return ChatResponse(chatId)
    }
}