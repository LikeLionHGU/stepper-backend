package com.likelionhgu.stepper.journal

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
        `when`("the member writes the journal entry") {
            every { journalRepository.save(any<Journal>()) } returns Journal("title", "content", mockk(), mockk())
            val request = JournalRequest("title", "content")

            then("the journal entry should be saved") {
                val journalId = journalService.writeJournal(mockk(), mockk(), request)

                journalId shouldNotBe null
            }
        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}