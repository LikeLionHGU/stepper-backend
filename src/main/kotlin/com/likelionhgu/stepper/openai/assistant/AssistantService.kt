package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.openai.assistant.thread.ThreadCreationRequest
import com.likelionhgu.stepper.openai.assistant.thread.ThreadResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AssistantService {

    @POST("/v1/threads")
    fun createThread(@Body requestBody: ThreadCreationRequest): Call<ThreadResponse>
}
