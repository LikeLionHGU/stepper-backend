package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.openai.assistant.thread.ThreadCreationRequest
import com.likelionhgu.stepper.openai.assistant.thread.ThreadMessageResponse
import com.likelionhgu.stepper.openai.assistant.thread.ThreadResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AssistantService {

    @POST("/v1/threads")
    fun createThread(@Body requestBody: ThreadCreationRequest): Call<ThreadResponse>

    @GET("/v1/threads/{thread_id}/messages")
    fun messagesOfThread(@Path("thread_id") threadId: String): Call<ThreadMessageResponse>
}
