package com.likelionhgu.stepper.goal.request

import com.likelionhgu.stepper.goal.Goal
import com.likelionhgu.stepper.goal.enums.GoalStatus
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class GoalUpdateRequest(

    @field:NotNull(message = "The title must not be null")
    val title: String?,

    @field:NotNull(message = "The status must not be null")
    val status: GoalStatus?,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate? = null,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate? = null,

    val thumbnail: String? = null,

    ) {
    fun toEntity() = Goal(
        title = title!!,
        startDate = startDate,
        endDate = endDate,
        thumbnail = thumbnail,
        status = status!!
    )
}
