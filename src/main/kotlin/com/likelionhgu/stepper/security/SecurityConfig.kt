package com.likelionhgu.stepper.security

import com.likelionhgu.stepper.security.csrf.SpaCsrfTokenRequestHandler
import com.likelionhgu.stepper.security.oauth2.CommonOAuth2Attribute
import com.likelionhgu.stepper.security.oauth2.CustomOAuth2UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oAuth2UserService: CustomOAuth2UserService
) {

    @Value("\${custom.host.client}")
    lateinit var clientHost: String

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
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(userService())
                    }
                    .successHandler(successHandler())
            }
            .csrf { csrf ->
                csrf
                    .ignoringRequestMatchers("/h2-console/**")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(SpaCsrfTokenRequestHandler())
            }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .build()
    }

    @Bean
    fun userService(): OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        return OAuth2UserService(oAuth2UserService::loadUser)
    }

    @Bean
    fun successHandler(): AuthenticationSuccessHandler {
        logger.info("Authentication has succeeded.")
        return AuthenticationSuccessHandler { request, response, authentication ->
            val userAttribute = authentication.principal as CommonOAuth2Attribute
            oAuth2UserService.saveUser(userAttribute)

            val csrfToken = request.getAttribute("_csrf") as CsrfToken
            // Render the token value to a cookie by causing the deferred token to be loaded
            csrfToken.token

            logger.info("Redirecting to $clientHost")
            response.sendRedirect(clientHost)
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf(clientHost)
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
        private const val CSRF_HEADER_NAME = "X-XSRF-TOKEN"
    }
}