package dev.iconpln.mims.data.remote.service

import com.google.gson.JsonObject
import dev.iconpln.mims.data.remote.response.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type:application/json")
    @POST("/users/login")
    suspend fun login(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("/users/sentOtp")
    suspend fun sendOtp(
        @Body body: Map<String, String>
    ): Response<GenericResponse>

    @Headers("Content-Type:application/json")
    @POST("/users/getOtpForgotPassword")
    suspend fun getOtpForgotPassword(
        @Body body: Map<String, String>
    ): Response<GenericResponse>

    @Headers("Content-Type:application/json")
    @POST("/users/isOtpValid")
    suspend fun otpValid(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("/users/isOtpValidRp")
    suspend fun otpValidForgotPassword(
        @Body body: Map<String, String>
    ): Response<GenericResponse>

    @Headers("Content-Type:application/json")
    @POST("/users/doForgotPassword")
    suspend fun changePassword(
        @Body body: Map<String, String>
    ): Response<GenericResponse>

    @Headers("Content-Type:application/json")
    @POST("/users/doChangePassword")
    suspend fun changePasswordProfile(
        @Body body: Map<String, String>
    ): Response<GenericResponse>

    @FormUrlEncoded
    @POST("/tracking/getTrackingHistory")
    suspend fun getTrackingHistory(
        @Field("sn") sn: String
    ): Response<TrackingHistoryResponse>

    @FormUrlEncoded
    @POST("/tracking/getTrackingHistoryDetail")
    suspend fun getDetailTrackingHistory(
        @Field("sn") sn: String,
        @Field("no_transaksi") noTransaksi: String,
        @Field("status") status: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("/reports/pemakaian/addSn")
    suspend fun addSn(
        @Field("no_transaksi") noTransaksi: String,
        @Field("no_material") noMaterial: String,
        @Field("serial_number") serialNumber: String,
        @Field("user_plant") userPlant: String,
        @Field("user_loc") userLoc: String
    ): Response<SnResponse>

    @FormUrlEncoded
    @POST("/reports/pemakaian/deleteSn")
    suspend fun deleteSn(
        @Field("no_transaksi") noTransaksi: String,
        @Field("no_material") noMaterial: String,
        @Field("serial_number") serialNumber: String,
        @Field("user_plant") userPlant: String,
        @Field("user_loc") userLoc: String
    ): Response<SnResponse>

    @FormUrlEncoded
    @POST("/reports/permintaan/addSn")
    suspend fun permintaanAddSn(
        @Field("no_repackaging") noRepackaging: String,
        @Field("no_material") noMaterial: String,
        @Field("serial_number") serialNumber: String,
        @Field("user_plant") userPlant: String,
        @Field("user_loc") userLoc: String
    ): Response<SnResponse>

    @FormUrlEncoded
    @POST("/reports/permintaan/deleteSn")
    suspend fun permintaanDeleteSn(
        @Field("no_repackaging") noRepackaging: String,
        @Field("no_material") noMaterial: String,
        @Field("serial_number") serialNumber: String
    ): Response<SnResponse>

    @FormUrlEncoded
    @POST("/reports/insertLokasi")
    suspend fun sendLokasi(
        @Field("do_mims") doMims: String,
        @Field("lokasi") lokasi: String
    ): Response<GenericResponse>

    @FormUrlEncoded
    @POST("/reports/deleteLokasi")
    suspend fun deleteLokasi(
        @Field("id_location") idLocation: String
    ): Response<GenericResponse>

    @FormUrlEncoded
    @POST("/reports/getLokasiByDoMims")
    suspend fun getLokasi(
        @Field("do_mims") doMims: String
    ): Response<LokasiResponse>

}