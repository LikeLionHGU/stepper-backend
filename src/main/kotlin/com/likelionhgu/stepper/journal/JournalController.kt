package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.GoalService
import com.likelionhgu.stepper.journal.request.JournalRequest
import com.likelionhgu.stepper.member.MemberService
import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JournalController(
    private val memberService: MemberService,
    private val journalService: JournalService,
    private val goalService: GoalService
) {

    @PostMapping("/v1/goals/{goalId}/journals")
    fun writeJournal(
        @AuthenticationPrincipal user: CommonOAuth2Attribute,
        @PathVariable goalId: String,
        @Valid @RequestBody journalRequest: JournalRequest
    ): ResponseEntity<Unit> {
        val member = memberService.memberInfo(user.oauth2UserId)
        val goal = goalService.goalInfo(goalId)
        journalService.writeJournal(member, goal, journalRequest)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}