package com.likelionhgu.stepper.member

import com.likelionhgu.stepper.common.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    @Column(unique = true)
    val oauth2Id: String,

    @Column
    val email: String,

    @Column
    val name: String? = null,

    @Column
    val picture: String? = null
) : BaseTime() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId = 0L
}