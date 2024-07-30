package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.journal.enums.JournalSortType
import com.likelionhgu.stepper.journal.request.JournalRequest
import com.likelionhgu.stepper.member.Member
import org.springframework.stereotype.Service

@Service
class JournalService(
    private val journalRepository: JournalRepository
) {

    /**
     * Write a journal entry for a goal.
     *
     * @param member The member who is the author of the journal entry.
     * @param goal The goal for which the journal entry is being written.
     */
    fun writeJournal(member: Member, goal: Goal, request: JournalRequest): Long {
        val journal = Journal(
            title = request.title!!,
            content = request.content.orEmpty(),
            member = member,
            goal = goal
        ).let(journalRepository::save)

        return journal.journalId
    }

    /**
     * Retrieve all journals for a goal.
     *
     * @param goal The goal for which to retrieve journals.
     * @param sortType The sorting type of the journals.
     * @return A list of journals for the goal ordered by the sorting type.
     */
    fun journalsOf(goal: Goal, sortType: JournalSortType, searchKeyword: String?): List<Journal> {
        return journalRepository.findAllByGoalWithQuery(goal, sortType.toSort(), searchKeyword)
    }
}
