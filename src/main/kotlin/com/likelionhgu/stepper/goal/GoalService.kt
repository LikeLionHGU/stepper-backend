package com.likelionhgu.stepper.goal

import com.likelionhgu.stepper.exception.GoalNotFoundException
import com.likelionhgu.stepper.exception.MemberNotFoundException
import com.likelionhgu.stepper.goal.enums.GoalSortType
import com.likelionhgu.stepper.goal.request.GoalRequest
import com.likelionhgu.stepper.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class GoalService(
    private val goalRepository: GoalRepository,
    private val memberRepository: MemberRepository,
) {
    fun createGoal(oauth2UserId: String, goalRequest: GoalRequest): Long {
        val member = memberRepository.findByOauth2Id(oauth2UserId)
            ?: throw MemberNotFoundException("The member with the oauth2 sub \"$oauth2UserId\" does not exist")

        val goal = Goal(
            goalRequest.title!!,
            goalRequest.startDate,
            goalRequest.endDate,
            goalRequest.thumbnail,
            member
        ).let(goalRepository::save)

        return goal.goalId
    }

    fun memberGoals(oauth2UserId: String, sortType: GoalSortType): List<Goal> {
        val member = memberRepository.findByOauth2Id(oauth2UserId)
            ?: throw MemberNotFoundException("The member with the oauth2 sub \"$oauth2UserId\" does not exist")

        return goalRepository.findAllByMember(member, sortType.toSort())
    }

    /**
     * Retrieves the detailed information of a goal based on its ID.
     *
     * @param goalId The ID of the goal.
     * @return Goal The `Goal` entity corresponding to the provided ID.
     * @throws GoalNotFoundException if no goal is found with the provided ID.
     */
    fun goalInfo(goalId: String): Goal {
        return goalRepository.findById(goalId.toLong()).getOrNull()
            ?: throw GoalNotFoundException("The goal with the id \"$goalId\" does not exist")
    }
}
