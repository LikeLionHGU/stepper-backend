package com.likelionhgu.stepper.security.oauth2

import com.likelionhgu.stepper.member.Member
import com.likelionhgu.stepper.member.MemberRepository
import com.likelionhgu.stepper.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val memberRepository: MemberRepository
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        logger.info("Loading user info from OAuth2 provider")
        val userInfo = DefaultOAuth2UserService().loadUser(userRequest)
        return matchProvider(userRequest, userInfo)
    }

    /**
     * Match the provider to the user info
     * @param userRequest OAuth2UserRequest object containing the user request data
     * @param userinfo OAuth2User object containing raw user info from the provider
     * @return OAuth2User object containing common attributes for the user
     * @see CommonOAuth2Attribute
     * @throws SecurityException.UnSupportedOAuth2ProviderException if the provider is not supported
     */
    private fun matchProvider(userRequest: OAuth2UserRequest, userinfo: OAuth2User): OAuth2User {
        val provider = userRequest.clientRegistration.registrationId
        logger.info("Matching provider: \"$provider\"")

        return when (provider) {
            "google" -> GoogleOAuth2User(userinfo)
            else -> throw SecurityException.UnSupportedOAuth2ProviderException("Unsupported OAuth2 provider: $provider")
        }
    }

    /**
     * Save the user to the database only if the user does not exist
     * @param userAttribute CommonOAuth2Attribute object containing common user attributes
     * @see CommonOAuth2Attribute
     * @see Member
     */
    fun saveUser(userAttribute: CommonOAuth2Attribute) {
        memberRepository.findByOauth2Id(userAttribute.oauth2UserId)
            ?: run {
                logger.info("Saving user info to the database: ${userAttribute.email}")
                val member = Member(
                    oauth2Id = userAttribute.oauth2UserId,
                    email = userAttribute.email,
                    name = userAttribute.username,
                    picture = userAttribute.picture
                )
                memberRepository.save(member)
            }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)
    }
}