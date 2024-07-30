package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.journal.enums.JournalSortType
import com.likelionhgu.stepper.journal.request.JournalRequest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class JournalServiceTest : BehaviorSpec({
    val journalRepository = mockk<JournalRepository>()
    val journalService = JournalService(journalRepository)

    given("A member who has created a goal") {
        every { journalRepository.save(any<Journal>()) } returns Journal("title", "content", mockk(), mockk())
        val request = JournalRequest("title", "content")

        `when`("the member writes the journal entry") {
            then("the journal entry should be saved") {
                val journalId = journalService.writeJournal(mockk(), mockk(), request)

                journalId shouldNotBe null
            }
        }
    }

    given("a member who has written some journal entries") {
        every { journalRepository.findAllByGoalWithQuery(any(), any(), any()) } returns listOf(
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

        `when`("the member requests to see the journal entries") {
            then("the journal entries should be returned") {
                val journals = journalService.journalsOf(mockk(), JournalSortType.NEWEST, null)

                journals.size shouldNotBe 0
            }
        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}