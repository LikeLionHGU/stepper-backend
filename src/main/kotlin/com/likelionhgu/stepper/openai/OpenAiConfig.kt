package com.likelionhgu.stepper.openai

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.likelionhgu.stepper.openai.assistant.AssistantService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Configuration
@EnableConfigurationProperties(OpenAiProperties::class)
class OpenAiConfig(
    private val openAiProperties: OpenAiProperties
) {

    @Bean
    fun assistantClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                chain.request().newBuilder()
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer ${openAiProperties.apiKey}")
                    .addHeader(OPENAI_BETA, "assistants=v2")
                    .build()
                    .let(chain::proceed)
            }
            .build()
    }

    @Bean
    fun assistantService(assistantClient: OkHttpClient): AssistantService {
        val snakeCasePolicy = Gson().newBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        return Retrofit.Builder()
            .client(assistantClient)
            .baseUrl(OPENAI_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(snakeCasePolicy))
            .build()
            .create(AssistantService::class.java)
    }

    companion object {
        private const val OPENAI_BASE_URL = "https://api.openai.com"
        private const val OPENAI_BETA = "OpenAI-Beta"
    }
}