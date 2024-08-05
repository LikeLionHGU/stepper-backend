package com.likelionhgu.stepper.websocket

import com.likelionhgu.stepper.security.SecurityConfig
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val clientProperties: SecurityConfig.ClientProperties
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.setApplicationDestinationPrefixes(DEFAULT_APP_DESTINATION)
        config.enableSimpleBroker(DEFAULT_BROKER_DESTINATION)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        val allowedOrigins = clientProperties.allowedOrigin.toTypedArray()
        registry.addEndpoint(ENDPOINT)
            .setAllowedOrigins(*allowedOrigins)
    }

    companion object {
        private const val DEFAULT_APP_DESTINATION = "/app"
        private const val DEFAULT_BROKER_DESTINATION = "/queue"
        private const val ENDPOINT = "/chatbot"
    }
}
