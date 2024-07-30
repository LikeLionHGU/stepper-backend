package com.likelionhgu.stepper.goal.request

import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class GoalRequest(

    @field:NotNull(message = "The title must not be null")
    val title: String?,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate? = null,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate? = null,

    val thumbnail: String? = null
)
