package com.likelionhgu.stepper.journal

import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.member.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Journal(

    @Column
    var title: String,

    @Column(length = 2048)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    val goal: Goal
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val journalId = 0L
}