package com.likelionhgu.stepper.journal;

import com.likelionhgu.stepper.goal.Goal
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface JournalRepository : JpaRepository<Journal, Long> {
    fun findAllByGoal(goal: Goal, sort: Sort): List<Journal>
}