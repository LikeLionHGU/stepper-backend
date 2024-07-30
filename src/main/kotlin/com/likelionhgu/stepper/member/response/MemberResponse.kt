package com.likelionhgu.stepper.member.response

import com.likelionhgu.stepper.member.Member

class MemberResponse(
    val name: String?,
    val thumbnail: String?
) {

    companion object {
        fun of(member: Member): MemberResponse {
            return MemberResponse(
                name = member.name,
                thumbnail = member.picture
            )
        }
    }
}
