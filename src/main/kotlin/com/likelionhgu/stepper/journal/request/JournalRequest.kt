package com.likelionhgu.stepper.journal.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class JournalRequest(

    @field:NotNull(message = "title is required")
    @field:NotBlank(message = "title cannot be blank")
    val title: String?,

    @field:NotNull(message = "content cannot be null")
    val content: String?
)
