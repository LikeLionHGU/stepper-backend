package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.GoalService
import com.likelionhgu.stepper.journal.enums.JournalSortType
import com.likelionhgu.stepper.journal.request.JournalRequest
import com.likelionhgu.stepper.journal.response.JournalResponseWrapper
import com.likelionhgu.stepper.member.MemberService
import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/v1/goals/{goalId}/journals")
    fun displayJournals(
        @PathVariable goalId: String,
        @RequestParam(required = false) sort: JournalSortType = JournalSortType.NEWEST,
        @RequestParam(required = false) q: String?
    ): ResponseEntity<JournalResponseWrapper> {
        // Handle query as null if it is an empty string
        val searchKeyword = q?.takeIf(String::isNotBlank)

        val goal = goalService.goalInfo(goalId)
        val journals = journalService.journalsOf(goal, sort, searchKeyword)

        val responseBody = JournalResponseWrapper.of(goal, journals)
        return ResponseEntity.ok(responseBody)
    }

    @GetMapping("/v1/journals/{journalId}")
    fun displayJournal(
        @PathVariable journalId: Long
    ): ResponseEntity<JournalResponseWrapper.JournalResponse> {
        val journal = journalService.journalInfo(journalId)

        val responseBody = JournalResponseWrapper.JournalResponse.of(journal)
        return ResponseEntity.ok(responseBody)
    }
}