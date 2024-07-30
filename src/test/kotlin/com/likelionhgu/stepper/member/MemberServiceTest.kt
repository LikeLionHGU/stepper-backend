package com.likelionhgu.stepper.member

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class MemberServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val memberService = MemberService(memberRepository)

    given("A member is created") {
        val oauth2Id = "123"
        val email = "johndoe@example.com"

        every { memberRepository.findByOauth2Id(oauth2Id) } returns Member(
            oauth2Id = oauth2Id,
            email = email,
            name = "John Doe",
            picture = "https://example.com/johndoe.jpg"
        )
        `when`("try to get the member profile by oauth2Id") {
            then("the member should have the correct information") {
                val member = memberService.memberInfo(oauth2Id)

                member.oauth2Id shouldBe oauth2Id
                member.email shouldBe email
            }
        }
    }
})