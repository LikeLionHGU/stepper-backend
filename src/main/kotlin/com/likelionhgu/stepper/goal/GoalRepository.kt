package com.likelionhgu.stepper.goal;

import com.likelionhgu.stepper.member.Member
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface GoalRepository : JpaRepository<Goal, Long> {

    fun findAllByMember(member: Member, sort: Sort): List<Goal>
}