package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.goal.GoalRepository
import com.likelionhgu.stepper.journal.enums.JournalSortType
import com.likelionhgu.stepper.member.Member
import com.likelionhgu.stepper.member.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
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
                val actual = journalRepository.findAllByGoal(goal, JournalSortType.NEWEST.toSort())

                actual[0].journalId shouldBe j2.journalId
                actual[1].journalId shouldBe j1.journalId
            }
        }

        `when`("the member requests to see the journal entries with oldest order") {
            then("the journal entries should be returned ordered by created date in ascending order") {
                val actual = journalRepository.findAllByGoal(goal, JournalSortType.OLDEST.toSort())

                actual[0].journalId shouldBe j1.journalId
                actual[1].journalId shouldBe j2.journalId
            }
        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}