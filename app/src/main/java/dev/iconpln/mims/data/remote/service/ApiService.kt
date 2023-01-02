package dev.iconpln.mims.data.remote.service

import dev.iconpln.mims.data.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type:application/json")
    @POST("/project-i-agodist-api//login/{userName}/{password}")
    suspend fun agoLogin(
        @Path("userName") userName: String,
        @Path("password") password: String
    ): Response<AgoLoginResponse>

    @Headers("Content-Type:application/json")
    @POST("/mims-service-api/loginmobile")
    suspend fun login(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @GET("mims-service-api/attMaterial/getMaterial/{sn}")
    suspend fun getDetailBySN(
        @Path("sn") sn: String
    ): Response<DetailSnResponse>

    @Headers("Content-Type:application/json")
    @GET("mims-service-api/sendmailtoken/{user_name}")
    suspend fun hitEmailResponse(
        @Path("user_name") username: String
    ): Response<HitEmailResponse>

    @Headers("Content-Type:application/json")
    @POST("mims-service-api/veriftoken")
    suspend fun sendOtp(
        @Body body: Map<String, String>
    ): Response<VerifyTokenResponse>
}