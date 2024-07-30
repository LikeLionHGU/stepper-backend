package com.likelionhgu.stepper.member

import com.likelionhgu.stepper.exception.MemberNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    /**
     * Retrieves the basic information of a member based on their OAuth2 ID.
     *
     * @param oauth2Id The OAuth2 ID of the member. This ID is used to uniquely identify and retrieve the member
     *                 from the repository.
     * @return Member The `Member` entity corresponding to the provided OAuth2 ID. This object contains the
     *                 basic information of the member.
     * @throws MemberNotFoundException if no member is found with the provided OAuth2 ID. This exception signals
     *                                  that the requested member could not be found in the repository.
     */
    fun memberInfo(oauth2Id: String): Member {
        return memberRepository.findByOauth2Id(oauth2Id)
            ?: throw MemberNotFoundException()
    }
}