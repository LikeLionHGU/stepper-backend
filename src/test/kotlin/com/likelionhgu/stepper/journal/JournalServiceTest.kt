package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.journal.enums.JournalSortType
import com.likelionhgu.stepper.journal.request.JournalRequest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

class JournalServiceTest : BehaviorSpec({
    val journalRepository = mockk<JournalRepository>()
    val journalService = JournalService(journalRepository)
    val goal = mockk<Goal>()

    given("A member who has created a goal") {
        every { journalRepository.save(any<Journal>()) } returns Journal(
            "title",
            "content",
            member = mockk(),
            goal = mockk()
        )
        every { goal.updateStreak(any<LocalDate>()) } returns Unit
        every { goal.journals } returns mutableListOf()
        val request = JournalRequest("title", "content")

        `when`("the member writes the journal entry") {
            then("the journal entry should be saved") {
                val journalId = journalService.writeJournal(mockk(), goal, request)

                journalId shouldNotBe null
            }
        }
    }

    given("a member who has written some journal entries") {
        every { journalRepository.findAllByGoalWithQuery(any(), any(), any()) } returns listOf(
            Journal(
                "title1",
                "content",
                member = mockk(),
                goal = mockk()
            ),
            Journal(
                "title2",
                "content",
                member = mockk(),
                goal = mockk()
            )
        )

        `when`("the member requests to see the journal entries") {
            then("the journal entries should be returned") {
                val journals = journalService.journalsOf(mockk(), JournalSortType.NEWEST, null)

                journals shouldHaveSize 2
            }
        }
    }

    given("a journal entry ID") {
        every { journalRepository.findById(any()).getOrNull() } returns Journal(
            "title",
            "content",
            member = mockk(),
            goal = mockk()
        )

        `when`("a member tries to read a journal entry") {
            then("the journal entry should be returned") {
                val journal = journalService.journalInfo(0L)

                journal shouldNotBe null
            }
        }

        `when`("a member tries to update a journal entry") {
            then("the journal entry should be updated") {
                val target = JournalRequest("Updated Title", "Updated Content")
                journalService.updateJournal(0L, target)

                val result = journalService.journalInfo(0L)

                result.title shouldBe "Updated Title"
                result.content shouldBe "Updated Content"
            }
        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}