package com.likelionhgu.stepper.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

/**
 * Represents a user authenticated by Google OAuth2
 * @param userinfo OAuth2User object containing raw user info from the provider
 */
data class GoogleOAuth2User(val userinfo: OAuth2User) : CommonOAuth2Attribute, OAuth2User {
    override val oauth2UserId: String = userinfo.getAttribute("sub")
        ?: throw SecurityException.MissingRequiredPropertiesException("sub is null")

    override val email: String = userinfo.getAttribute("email")
        ?: throw SecurityException.MissingRequiredPropertiesException("email is null")

    override val username: String? = userinfo.getAttribute("name")
    override val picture: String? = userinfo.getAttribute("picture")

    override fun getName(): String? = username
    override fun getAttributes(): MutableMap<String, Any> = userinfo.attributes
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = userinfo.authorities
}
