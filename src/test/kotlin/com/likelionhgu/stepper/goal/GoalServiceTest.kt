package com.likelionhgu.stepper.goal

import com.likelionhgu.stepper.goal.enums.GoalSortType
import com.likelionhgu.stepper.goal.enums.GoalStatus
import com.likelionhgu.stepper.goal.request.GoalRequest
import com.likelionhgu.stepper.goal.request.GoalUpdateRequest
import com.likelionhgu.stepper.member.Member
import com.likelionhgu.stepper.member.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@SpringBootTest
@Transactional
class GoalServiceTest(
    private val goalService: GoalService,
    private val goalRepository: GoalRepository,
    private val memberRepository: MemberRepository
) : BehaviorSpec({
    val oauth2UserId = "123"
    val goalTitle = "Effective Kotlin"

    beforeSpec {
        val member = Member(oauth2UserId, "")
        memberRepository.save(member)
    }

    given("Required information for setting a goal") {
        val request = GoalRequest(title = goalTitle)

        `when`("the goal is created") {
            then("the goal should be in the database") {
                val goalId = goalService.createGoal(oauth2UserId, request)
                val goal = goalRepository.findById(goalId).getOrNull()

                goal shouldNotBe null
                goal?.title shouldBe goalTitle
            }
        }
    }

    given("Goals more than one") {
        val request1 = GoalRequest(title = "A $goalTitle")
        val request2 = GoalRequest(title = "B $goalTitle")

        val goalId1 = goalService.createGoal(oauth2UserId, request1)
        val goalId2 = goalService.createGoal(oauth2UserId, request2)

        `when`("the goals are retrieved with an order of recent") {
            then("the goals should be sorted by recent") {
                val goals = goalService.memberGoals(oauth2UserId, GoalSortType.NEWEST)

                goals[0].goalId shouldBe goalId2
                goals[1].goalId shouldBe goalId1
            }
        }

        `when`("the goals are retrieved with an order of ascending") {
            then("the goals should be sorted by ascending") {
                val goals = goalService.memberGoals(oauth2UserId, GoalSortType.ASC)

                goals[0].goalId shouldBe goalId1
                goals[1].goalId shouldBe goalId2
            }
        }

        `when`("the goals are retrieved with an order of descending") {
            then("the goals should be sorted by ascending") {
                val goals = goalService.memberGoals(oauth2UserId, GoalSortType.DESC)

                goals[0].goalId shouldBe goalId2
                goals[1].goalId shouldBe goalId1
            }
        }

        `when`("the goal is updated") {
            then("the goal should be updated") {
                val request = GoalUpdateRequest(
                    title = goalTitle,
                    status = GoalStatus.CLOSED
                )
                goalService.updateGoal(goalId1, request)

                val goal = goalService.goalInfo(goalId1)
                goal shouldNotBe null
                goal.status shouldBe GoalStatus.CLOSED
            }
        }

        `when`("the goal is deleted") {
            then("the goal should be deleted") {
                goalService.deleteGoal(goalId1)

                val goal = goalRepository.findById(goalId1).getOrNull()
                goal shouldBe null
            }
        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}