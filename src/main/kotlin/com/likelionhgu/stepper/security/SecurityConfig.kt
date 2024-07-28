package com.likelionhgu.stepper.security

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(
    private val oAuth2UserService: CustomOAuth2UserService
) {

    @Value("\${custom.host.client}")
    lateinit var clientHost: String

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authz ->
                authz.requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2.userInfoEndpoint { userInfo ->
                    userInfo.userService(userService())
                }
                oauth2.successHandler(successHandler())
            }
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/h2-console/**")
            }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .build()
    }

    private fun userService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return OAuth2UserService(oAuth2UserService::loadUser)
    }

    private fun successHandler(): AuthenticationSuccessHandler {
        logger.info("Authentication has succeeded.")
        return AuthenticationSuccessHandler { _, response, authentication ->
            val userAttribute = authentication.principal as CommonOAuth2Attribute
            oAuth2UserService.saveUser(userAttribute)

            logger.info("Redirecting to $clientHost")
            response.sendRedirect(clientHost)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}