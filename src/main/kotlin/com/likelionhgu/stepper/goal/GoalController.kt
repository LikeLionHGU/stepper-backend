package com.likelionhgu.stepper.goal

import com.likelionhgu.stepper.goal.enums.GoalSortType
import com.likelionhgu.stepper.goal.request.GoalRequest
import com.likelionhgu.stepper.goal.request.GoalUpdateRequest
import com.likelionhgu.stepper.goal.response.GoalResponseWrapper
import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/v1/goals")
    fun displayMyGoals(
        @AuthenticationPrincipal user: CommonOAuth2Attribute,
        @RequestParam(required = false) sort: GoalSortType = GoalSortType.NEWEST
    ): ResponseEntity<GoalResponseWrapper> {
        val goals = goalService.memberGoals(user.oauth2UserId, sort)

        val responseBody = GoalResponseWrapper.of(goals)
        return ResponseEntity.ok(responseBody)
    }

    @PutMapping("/v1/goals/{goalId}")
    fun updateGoal(
        @PathVariable goalId: Long,
        @RequestBody goalRequest: GoalUpdateRequest,
    ): ResponseEntity<Unit> {
        goalService.updateGoal(goalId, goalRequest)
        return ResponseEntity.ok().build()
    }
}