package dev.iconpln.mims.data.remote.service

import dev.iconpln.mims.data.remote.response.DetailSnResponse
import dev.iconpln.mims.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type:application/json")
    @POST("/project-i-agodist-api//login/{userName}/{password}")
    suspend fun anotherLogin(
        @Path("userName") userName: String,
        @Path("password") password: String
    ): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @GET("project-i-agodist-api/getInspeksiMobile/{sn}")
    suspend fun getDetailBySN(
        @Path("sn") sn: String
    ): Response<DetailSnResponse>
}