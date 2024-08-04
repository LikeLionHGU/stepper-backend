package com.likelionhgu.stepper.openai.completion

import com.likelionhgu.stepper.openai.completion.request.CompletionRequest
import com.likelionhgu.stepper.openai.completion.response.CompletionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CompletionService {

    @POST("/v1/chat/completions")
    fun createChatCompletion(@Body completionRequest: CompletionRequest): Call<CompletionResponse>

}