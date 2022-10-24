package dev.iconpln.mims.data.remote.service

import dev.iconpln.mims.data.remote.response.DetailSnResponse
import dev.iconpln.mims.data.remote.response.HitEmailResponse
import dev.iconpln.mims.data.remote.response.LoginResponse
import dev.iconpln.mims.data.remote.response.VerifyTokenResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type:application/json")
    @POST("/mims-service-api/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @GET("project-i-agodist-api/getInspeksiMobile/{sn}")
    suspend fun getDetailBySN(
        @Path("sn") sn: String
    ): Response<DetailSnResponse>

    @Headers("Content-Type:application/json")
    @GET("mims-service-api/sendmailtoken/{username}")
    suspend fun hitEmailResponse(
        @Path("username") username: String
    ): Response<HitEmailResponse>

    @Headers("Content-Type:application/json")
    @POST("mims-service-api/veriftoken")
    suspend fun sendOtp(
        @Body body: Map<String, String>
    ): Response<VerifyTokenResponse>
}