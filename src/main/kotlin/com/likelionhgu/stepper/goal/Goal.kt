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

    @Column
    var startDate: LocalDate? = null,

    @Column
    var endDate: LocalDate? = null,

    @Column(length = 512)
    var thumbnail: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
) : BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val goalId = 0L

    @Column
    var status = GoalStatus.OPEN

    @Column
    var streak = 0
}