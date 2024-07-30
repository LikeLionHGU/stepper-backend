package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.goal.GoalRepository
import com.likelionhgu.stepper.journal.enums.JournalSortType
import com.likelionhgu.stepper.member.Member
import com.likelionhgu.stepper.member.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class JournalRepositoryTest(
    private val memberRepository: MemberRepository,
    private val goalRepository: GoalRepository,
    private val journalRepository: JournalRepository
) : BehaviorSpec({
    val member = memberRepository.save(Member("email", "name", "picture"))
    val goal = goalRepository.save(Goal(title = "title", member = member))

    given("multiple journal entries") {
        val j1 = journalRepository.save(Journal("title1", "content", member, goal))
        val j2 = journalRepository.save(Journal("title2", "content", member, goal))

        `when`("the member requests to see the journal entries with default order") {
            then("the journal entries should be returned ordered by created date in descending order") {
                val actual = journalRepository.findAllByGoalWithQuery(goal, JournalSortType.NEWEST.toSort(), null)

                actual[0].journalId shouldBe j2.journalId
                actual[1].journalId shouldBe j1.journalId
            }
        }

        `when`("the member requests to see the journal entries with oldest order") {
            then("the journal entries should be returned ordered by created date in ascending order") {
                val actual = journalRepository.findAllByGoalWithQuery(goal, JournalSortType.OLDEST.toSort(), null)

                actual[0].journalId shouldBe j1.journalId
                actual[1].journalId shouldBe j2.journalId
            }
        }

        `when`("the member searches for a journal entry with a keyword 'title1'") {
            then("the journal entries should be returned that contain the keyword") {
                val actual = journalRepository.findAllByGoalWithQuery(goal, JournalSortType.NEWEST.toSort(), "title1")

                actual.size shouldBe 1
                actual[0].journalId shouldBe j1.journalId
            }
        }

        `when`("the member searches for a journal entry with a keyword 'content'") {
            then("the journal entries should be returned that contain the keyword") {
                val actual = journalRepository.findAllByGoalWithQuery(goal, JournalSortType.NEWEST.toSort(), "content")

                actual.size shouldBe 2
            }
        }

        `when`("the member searches for a journal entry with a keyword that does not exist") {
            then("no journal entries should be returned") {
                val actual = journalRepository.findAllByGoalWithQuery(goal, JournalSortType.NEWEST.toSort(), "keyword")

                actual.size shouldBe 0
            }
        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}