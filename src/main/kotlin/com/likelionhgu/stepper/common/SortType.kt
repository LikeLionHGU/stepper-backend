package com.likelionhgu.stepper.common

import org.springframework.data.domain.Sort

interface SortType {

    /**
     * Convert the sorting type to a Spring Data [Sort] object.
     *
     * @return A [Sort] object representing the sorting type.
     */
    fun toSort(): Sort
}