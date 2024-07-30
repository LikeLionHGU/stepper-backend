package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.GoalService
import com.likelionhgu.stepper.member.MemberService
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(JournalController::class)
class JournalControllerTest(
    private val mockMvc: MockMvc,

    @MockkBean
    private val journalService: JournalService,

    @MockkBean
    private val memberService: MemberService,

    @MockkBean
    private val goalService: GoalService
) : BehaviorSpec({

    given("A member who has created a goal") {
        `when`("the member writes the journal entry with a title and content") {
            every { memberService.memberInfo(any()) } returns mockk()
            every { goalService.goalInfo(any()) } returns mockk()
            every { journalService.writeJournal(any(), any(), any()) } returns 1L
        }
        then("the request should be successful") {
            mockMvc.post("/v1/goals/1/journals") {
                with(csrf())
                with(oauth2Login())
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": "title", "content": "content"}"""
            }
                .andDo { print() }
                .andExpect { status { isCreated() } }
        }

        `when`("the member writes the journal entry without a title") {
            every { memberService.memberInfo(any()) } returns mockk()
            every { goalService.goalInfo(any()) } returns mockk()
            every { journalService.writeJournal(any(), any(), any()) } returns 1L
        }
        then("the request should be bad request") {
            mockMvc.post("/v1/goals/1/journals") {
                with(csrf())
                with(oauth2Login())
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": null, "content": "content"}"""
            }
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }
    }
}) {
}