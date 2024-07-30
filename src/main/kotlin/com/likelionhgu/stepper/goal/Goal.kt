package com.likelionhgu.stepper.goal

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
    val title: String,

    @Column
    val startDate: LocalDate? = null,

    @Column
    val endDate: LocalDate? = null,

    @Column
    val thumbnail: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var goalId = 0L
}