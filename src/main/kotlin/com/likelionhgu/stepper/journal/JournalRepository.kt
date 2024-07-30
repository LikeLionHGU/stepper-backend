package com.likelionhgu.stepper.journal;

import org.springframework.data.jpa.repository.JpaRepository

interface JournalRepository : JpaRepository<Journal, Long> {
}