package com.likelionhgu.stepper.security

import com.likelionhgu.stepper.security.SecurityConfig.ClientProperties
import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import com.likelionhgu.stepper.security.oauth2.CustomOAuth2UserService
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableConfigurationProperties(ClientProperties::class)
@EnableWebSecurity
class SecurityConfig(
    private val oAuth2UserService: CustomOAuth2UserService,
    private val clientProperties: ClientProperties,
) {

    @ConfigurationProperties(prefix = "custom.client")
    data class ClientProperties(
        val redirectUri: String,
        val allowedOrigin: List<String>,
    )

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint { it.userService(oAuth2UserService()) }
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler())
            }
            .csrf { it.ignoringRequestMatchers("/h2-console/**") }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .logout { it.logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)) }
            .exceptionHandling { it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .build()
    }

    private fun oAuth2UserService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return OAuth2UserService(oAuth2UserService::loadUser)
    }

    private fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
        logger.info("Authentication has succeeded.")
        return AuthenticationSuccessHandler { _, response, authentication ->
            val userAttribute = authentication.principal as CommonOAuth2Attribute
            oAuth2UserService.saveUser(userAttribute)

            logger.info("Redirecting to ${clientProperties.redirectUri}")
            response.sendRedirect(clientProperties.redirectUri)
        }
    }

    private fun authenticationFailureHandler(): AuthenticationFailureHandler {
        return AuthenticationFailureHandler { _, response, exception ->
            logger.info("Authentication has failed: ${exception.message}")
            response.status = HttpStatus.UNAUTHORIZED.value()
        }
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = clientProperties.allowedOrigin
            allowedMethods = listOf("POST", "GET", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf(HttpHeaders.CONTENT_TYPE, CSRF_HEADER_NAME)
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)
        private const val CSRF_HEADER_NAME = "X-CSRF-TOKEN"
    }
}