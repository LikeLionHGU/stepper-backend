package com.likelionhgu.stepper.journal.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.goal.response.GoalResponseWrapper
import com.likelionhgu.stepper.journal.Journal
import java.time.LocalDate

@JsonInclude(NON_NULL)
data class JournalResponseWrapper(
    val goal: GoalResponseWrapper.GoalResponse,
    val journals: List<JournalResponse>
) {

    data class JournalResponse(
        val journalId: Long,
        val title: String,
        val content: String?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd", timezone = "Asia/Seoul")
        val createdDate: LocalDate
    ) {
        companion object {
            fun of(journal: Journal): JournalResponse = JournalResponse(
                journalId = journal.journalId,
                title = journal.title,
                content = journal.content,
                createdDate = journal.createdDate.toLocalDate()
            )
        }
    }

    companion object {
        fun of(goal: Goal, journals: List<Journal>): JournalResponseWrapper {
            return JournalResponseWrapper(
                goal = GoalResponseWrapper.GoalResponse.of(goal),
                journals = journals.map {
                    JournalResponse(
                        journalId = it.journalId,
                        title = it.title,
                        content = null,
                        createdDate = it.createdDate.toLocalDate()
                    )
                }
            )
        }
    }
}
