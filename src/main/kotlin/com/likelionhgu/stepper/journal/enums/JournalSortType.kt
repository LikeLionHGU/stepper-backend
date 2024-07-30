package com.likelionhgu.stepper.journal.enums

import com.likelionhgu.stepper.common.SortType
import com.likelionhgu.stepper.journal.Journal
import com.likelionhgu.stepper.journal.enums.JournalSortType.NEWEST
import com.likelionhgu.stepper.journal.enums.JournalSortType.OLDEST
import org.springframework.data.domain.Sort

/**
 * Represents the sorting type of journals.
 *
 * @property NEWEST Sort journals by newest.
 * @property OLDEST Sort journals by oldest.
 */
enum class JournalSortType : SortType {
    NEWEST,
    OLDEST;

    override fun toSort(): Sort {
        return when (this) {
            NEWEST -> Sort.by(Sort.Order.desc(Journal::createdDate.name))
            OLDEST -> Sort.by(Sort.Order.asc(Journal::createdDate.name))
        }
    }
}
