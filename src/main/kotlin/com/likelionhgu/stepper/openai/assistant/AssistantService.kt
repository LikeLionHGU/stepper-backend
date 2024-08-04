package com.likelionhgu.stepper.openai.assistant

import com.likelionhgu.stepper.openai.assistant.AssistantResponseWrapper.AssistantResponse
import com.likelionhgu.stepper.openai.assistant.message.MessageRequest
import com.likelionhgu.stepper.openai.assistant.run.RunRequest
import com.likelionhgu.stepper.openai.assistant.run.RunResponse
import com.likelionhgu.stepper.openai.assistant.message.MessageResponseWrapper
import com.likelionhgu.stepper.openai.assistant.message.MessageResponseWrapper.MessageResponse
import com.likelionhgu.stepper.openai.assistant.thread.ThreadCreationRequest
import com.likelionhgu.stepper.openai.assistant.thread.ThreadResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AssistantService {

    @POST("/v1/assistants")
    fun createAssistant(@Body requestBody: AssistantRequest): Call<AssistantResponse>

    @GET("/v1/assistants")
    fun listAssistants(): Call<AssistantResponseWrapper>

    @POST("/v1/threads")
    fun createThread(@Body requestBody: ThreadCreationRequest): Call<ThreadResponse>

    @GET("/v1/threads/{thread_id}/messages")
    fun listMessagesOf(@Path("thread_id") threadId: String): Call<MessageResponseWrapper>

    @POST("/v1/threads/{thread_id}/messages")
    fun createMessageOf(@Path("thread_id") threadId: String, @Body message: MessageRequest): Call<MessageResponse>

    @POST("/v1/threads/{thread_id}/runs")
    fun createRunOf(@Path("thread_id") threadId: String, @Body runRequest: RunRequest): Call<RunResponse>

    @GET("/v1/threads/{thread_id}/runs/{run_id}")
    fun retrieveRunOf(@Path("thread_id") threadId: String, @Path("run_id") runId: String): Call<RunResponse>

}
