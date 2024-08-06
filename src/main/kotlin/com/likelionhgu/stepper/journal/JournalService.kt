package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.exception.JournalNotFoundException
import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.journal.enums.JournalSortType
import com.likelionhgu.stepper.journal.request.JournalRequest
import com.likelionhgu.stepper.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
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
            thumbnail = request.thumbnail,
            member = member,
            goal = goal
        ).let(journalRepository::save)
        goal.updateStreak(journal.createdDate.toLocalDate())

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

    /**
     * Retrieve a journal entry by its ID.
     *
     * @param journalId The ID of the journal entry to retrieve.
     * @return The journal entry with the given ID.
     * @throws JournalNotFoundException If no journal entry is found with the given ID.
     */
    fun journalInfo(journalId: Long): Journal {
        return journalRepository.findById(journalId).getOrNull()
            ?: throw JournalNotFoundException("Journal not found with ID: $journalId")
    }

    fun updateJournal(journalId: Long, journalRequest: JournalRequest) {
        val sourceJournal = journalInfo(journalId)
        val targetJournal = journalRequest.toEntity()
        sourceJournal.update(targetJournal)
    }

    fun deleteJournal(journalId: Long) {
        journalRepository.deleteById(journalId)
    }
}
