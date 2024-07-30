package com.likelionhgu.stepper.journal;

import com.likelionhgu.stepper.goal.Goal
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface JournalRepository : JpaRepository<Journal, Long> {

    @Query(
        "select j from Journal j where j.goal = :goal " +
                "and ((:searchKeyword is null or j.title like %:searchKeyword%) " +
                "or (:searchKeyword is null or j.content like %:searchKeyword%))"
    )
    fun findAllByGoalWithQuery(goal: Goal, sort: Sort, searchKeyword: String?): List<Journal>
}