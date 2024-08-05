package com.likelionhgu.stepper.goal

import com.likelionhgu.stepper.common.BaseTime
import com.likelionhgu.stepper.goal.enums.GoalStatus
import com.likelionhgu.stepper.member.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity
class Goal(

    @Column
    var title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member? = null,

    @Column
    var startDate: LocalDate? = null,

    @Column
    var endDate: LocalDate? = null,

    @Column(length = 512)
    var thumbnail: String? = null,

    @Column
    var status: GoalStatus = GoalStatus.OPEN
) : BaseTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val goalId: Long = 0L

    @Column
    var streak: Int = 0

    fun update(targetGoal: Goal) {
        title = targetGoal.title
        startDate = targetGoal.startDate
        endDate = targetGoal.endDate
        thumbnail = targetGoal.thumbnail
        status = targetGoal.status
    }
}