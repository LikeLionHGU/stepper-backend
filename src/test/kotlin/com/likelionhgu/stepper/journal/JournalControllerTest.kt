package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.Goal
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
import org.springframework.test.web.servlet.get
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

    given("a member who has created a goal") {
        every { memberService.memberInfo(any()) } returns mockk()
        every { goalService.goalInfo(any()) } returns mockk()
        every { journalService.writeJournal(any(), any(), any()) } returns 1L

        `when`("the member writes the journal entry with a title and content") {
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
        }

        `when`("the member writes the journal entry without a title") {
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
    }

    given("a member who has created a goal and written some journal entries") {
        every { goalService.goalInfo(any()) } returns Goal(title = "title", member = mockk())
        every { journalService.journalsOf(any(), any()) } returns listOf(
            Journal(
                "title1",
                "content",
                mockk(),
                mockk()
            ),
            Journal(
                "title2",
                "content",
                mockk(),
                mockk()
            )
        )

        `when`("the member tries to read a list of journal entries") {
            then("the request should be successful") {
                mockMvc.get("/v1/goals/1/journals") {
                    with(oauth2Login())
                }
                    .andDo { print() }
                    .andExpect { status { isOk() } }
            }
        }
    }
}) {
}