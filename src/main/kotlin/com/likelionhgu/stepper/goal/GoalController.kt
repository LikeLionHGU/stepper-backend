package com.likelionhgu.stepper.goal

import com.likelionhgu.stepper.goal.request.GoalRequest
import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GoalController(
    private val goalService: GoalService
) {

    @PostMapping("/v1/goals")
    fun createGoal(
        @AuthenticationPrincipal user: CommonOAuth2Attribute,
        @Valid @RequestBody goalRequest: GoalRequest
    ): ResponseEntity<Unit> {
        goalService.createGoal(user.oauth2UserId, goalRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}