package com.likelionhgu.stepper.goal

import com.likelionhgu.stepper.goal.request.GoalRequest
import com.likelionhgu.stepper.member.Member
import com.likelionhgu.stepper.member.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import kotlin.jvm.optionals.getOrNull

@SpringBootTest
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
            val goalId = goalService.createGoal(oauth2UserId, request)

            then("the goal should be in the database") {
                val goal = goalRepository.findById(goalId).getOrNull()

                goal shouldNotBe null
                goal?.title shouldBe goalTitle
            }
        }
    }
})