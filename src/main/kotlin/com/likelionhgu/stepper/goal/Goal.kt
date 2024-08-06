package com.likelionhgu.stepper.goal

import com.likelionhgu.stepper.common.BaseTime
import com.likelionhgu.stepper.goal.enums.GoalStatus
import com.likelionhgu.stepper.journal.Journal
import com.likelionhgu.stepper.member.Member
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
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

    @Column
    var lastEntryDate: LocalDate? = null

    @OneToMany(mappedBy = "goal", cascade = [CascadeType.REMOVE])
    val journals: MutableList<Journal> = mutableListOf()

    fun update(targetGoal: Goal) {
        title = targetGoal.title
        startDate = targetGoal.startDate
        endDate = targetGoal.endDate
        thumbnail = targetGoal.thumbnail
        status = targetGoal.status
    }

    fun updateStreak(entryDate: LocalDate) {
        if (isConsecutive(entryDate)) {
            streak++
        }
        lastEntryDate = entryDate
    }

    private fun isConsecutive(entryDate: LocalDate): Boolean {
        return streak == 0 || lastEntryDate?.plusDays(1) == entryDate
    }

    fun refreshStreak(): Goal {
        if (lastEntryDate?.plusDays(1) != LocalDate.now()) {
            streak = 0
        }
        return this
    }
}