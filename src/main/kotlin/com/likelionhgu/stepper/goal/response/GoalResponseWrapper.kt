package com.likelionhgu.stepper.goal.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.goal.enums.GoalStatus
import java.time.LocalDate

data class GoalResponseWrapper(
    val goals: List<GoalResponse>
) {
    data class GoalResponse(
        val goalId: Long,
        val title: String,
        val status: GoalStatus,
        val streak: Int,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd", timezone = "Asia/Seoul")
        val startDate: LocalDate?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd", timezone = "Asia/Seoul")
        val endDate: LocalDate?,

        val thumbnail: String?,
    ) {
        companion object {
            fun of(goal: Goal): GoalResponse = GoalResponse(
                goal.goalId,
                goal.title,
                goal.status,
                goal.streak,
                goal.startDate,
                goal.endDate,
                goal.thumbnail
            )
        }
    }

    companion object {
        fun of(goals: List<Goal>): GoalResponseWrapper {
            return goals.map { goal ->
                GoalResponse(
                    goal.goalId,
                    goal.title,
                    goal.status,
                    goal.streak,
                    goal.startDate,
                    goal.endDate,
                    goal.thumbnail
                )
            }.run(::GoalResponseWrapper)
        }
    }
}
