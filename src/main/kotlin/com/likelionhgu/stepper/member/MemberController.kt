package com.likelionhgu.stepper.member

import com.likelionhgu.stepper.member.response.MemberResponse
import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/v1/members/my")
    fun getMyInfo(
        @AuthenticationPrincipal user: CommonOAuth2Attribute
    ): ResponseEntity<MemberResponse> {
        val member = memberService.memberInfo(user.oauth2UserId)
        val responseBody = MemberResponse.of(member)

        return ResponseEntity.ok(responseBody)
    }
}