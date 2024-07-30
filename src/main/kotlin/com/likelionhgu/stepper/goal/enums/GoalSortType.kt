package com.likelionhgu.stepper.goal.enums

import com.likelionhgu.stepper.common.SortType
import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.goal.enums.GoalSortType.*
import org.springframework.data.domain.Sort

/**
 * Represents the different sorting types for goals.
 *
 * @property NEWEST Sorts goals by the newest first (default).
 * @property ASC Sorts goals in ascending order by title.
 * @property DESC Sorts goals in descending order by title.
 */
enum class GoalSortType : SortType {
    NEWEST,
    ASC,
    DESC;

    override fun toSort(): Sort {
        return when (this) {
            NEWEST -> Sort.by(Sort.Order.desc(Goal::createdDate.name))
            ASC -> Sort.by(Sort.Order.asc(Goal::title.name))
            DESC -> Sort.by(Sort.Order.desc(Goal::title.name))
        }
    }
}