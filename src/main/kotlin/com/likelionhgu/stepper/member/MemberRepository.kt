package com.likelionhgu.stepper.member;

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByOauth2Id(oauth2UserId: String): Member?
}