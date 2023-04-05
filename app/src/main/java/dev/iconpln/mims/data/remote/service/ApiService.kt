package dev.iconpln.mims.data.remote.service

import com.google.gson.JsonObject
import dev.iconpln.mims.data.remote.response.*
import dev.iconpln.mims.utils.SharedPrefsUtils
import okhttp3.RequestBody
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
    suspend fun forgotPasswordPassword(
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

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MTk2LFwidXNlcl9uYW1lXCI6XCI1MjUwMC5BR0FcIixcInVzZXJfcGxhbnRcIjpcIjUyMjFcIixcInVzZXJfc2xvY1wiOlwiMjEzMFwiLFwidXNlcl9yb2xlXCI6XCIzXCIsXCJlbWFpbFwiOlwiYWppc2V0aWFqaTEwNkBnbWFpbC5jb21cIn0iLCJleHAiOjE3MTE4OTY5ODUsImlhdCI6MTY4MDM2MDk4NX0._-EZj7gvH36ooZ9THEOJz1eAPOQNJ-VhVmlmYUHTHnZmaFfJgQY9xqltAtVtrf3xlk-2YEzIscvgzPHLzsD6BQ")
    @GET("/aktivasimaterial/getMonitoringAktivasiMaterial")
    suspend fun getMonitoringAktivasiMaterial(
        @Query("status") status: String,
        @Query("filter") sn: String
    ): Response<MonitoringAktivasiMaterialResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MTk2LFwidXNlcl9uYW1lXCI6XCI1MjUwMC5BR0FcIixcInVzZXJfcGxhbnRcIjpcIjUyMjFcIixcInVzZXJfc2xvY1wiOlwiMjEzMFwiLFwidXNlcl9yb2xlXCI6XCIzXCIsXCJlbWFpbFwiOlwiYWppc2V0aWFqaTEwNkBnbWFpbC5jb21cIn0iLCJleHAiOjE3MTE4OTY5ODUsImlhdCI6MTY4MDM2MDk4NX0._-EZj7gvH36ooZ9THEOJz1eAPOQNJ-VhVmlmYUHTHnZmaFfJgQY9xqltAtVtrf3xlk-2YEzIscvgzPHLzsD6BQ")
    @POST("/aktivasimaterial/insertMaterialRegistrasi")
    suspend fun insertMaterialRegistrasi(
        @Body requestBody: RequestBodyRegisSn
    ): Response<InsertMaterialRegistrasiResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MTk4LFwidXNlcl9uYW1lXCI6XCI1MjUwMC5NS0FcIixcInVzZXJfcGxhbnRcIjpcIjUyMjFcIixcInVzZXJfc2xvY1wiOlwiMjEzMFwiLFwidXNlcl9yb2xlXCI6XCI1XCIsXCJlbWFpbFwiOlwiTUlNc0dPd2VzMjAyM0BnbWFpbC5jb21cIn0iLCJleHAiOjE3MTIwMTkyMDQsImlhdCI6MTY4MDQ4MzIwNH0.t-_-sa3pKGKuUPBHscl0XBdGORDRE6RuahnWGBGvMi89yXXs5nfJ8Q5PlLGIHitncT0TmrLlDiBeYObmjFE8kA")
    @GET("/aktivasimaterial/getMaterialAktivasi")
    suspend fun getMaterialAktivasi(
        @Query("status") status: String
    ): Response<GetMaterialAktivasiResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MTk4LFwidXNlcl9uYW1lXCI6XCI1MjUwMC5NS0FcIixcInVzZXJfcGxhbnRcIjpcIjUyMjFcIixcInVzZXJfc2xvY1wiOlwiMjEzMFwiLFwidXNlcl9yb2xlXCI6XCI1XCIsXCJlbWFpbFwiOlwiTUlNc0dPd2VzMjAyM0BnbWFpbC5jb21cIn0iLCJleHAiOjE3MTIwMTkyMDQsImlhdCI6MTY4MDQ4MzIwNH0.t-_-sa3pKGKuUPBHscl0XBdGORDRE6RuahnWGBGvMi89yXXs5nfJ8Q5PlLGIHitncT0TmrLlDiBeYObmjFE8kA")
    @POST("/aktivasimaterial/aktivasiSerialNumber")
    suspend fun aktivasiMaterial(
        @Body requestBody: RequestBodyAktivMaterial
    ): Response<AktivasiSerialNumberResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MTk4LFwidXNlcl9uYW1lXCI6XCI1MjUwMC5NS0FcIixcInVzZXJfcGxhbnRcIjpcIjUyMjFcIixcInVzZXJfc2xvY1wiOlwiMjEzMFwiLFwidXNlcl9yb2xlXCI6XCI1XCIsXCJlbWFpbFwiOlwiTUlNc0dPd2VzMjAyM0BnbWFpbC5jb21cIn0iLCJleHAiOjE3MTIwMTkyMDQsImlhdCI6MTY4MDQ4MzIwNH0.t-_-sa3pKGKuUPBHscl0XBdGORDRE6RuahnWGBGvMi89yXXs5nfJ8Q5PlLGIHitncT0TmrLlDiBeYObmjFE8kA")
    @GET("/aktivasimaterial/getMaterialRegistrasiDetailByDate")
    suspend fun getMaterialRegistrasiDetailByDate(
        @Query("tgl_registrasi") tgl_registrasi: String,
        @Query("status") status: String,
        @Query("filter") sn: String,
        @Query("nomor_material") no_material: String
    ): Response<GetMaterialRegistrasiDetailByDateResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MTk4LFwidXNlcl9uYW1lXCI6XCI1MjUwMC5NS0FcIixcInVzZXJfcGxhbnRcIjpcIjUyMjFcIixcInVzZXJfc2xvY1wiOlwiMjEzMFwiLFwidXNlcl9yb2xlXCI6XCI1XCIsXCJlbWFpbFwiOlwiTUlNc0dPd2VzMjAyM0BnbWFpbC5jb21cIn0iLCJleHAiOjE3MTIwMTkyMDQsImlhdCI6MTY4MDQ4MzIwNH0.t-_-sa3pKGKuUPBHscl0XBdGORDRE6RuahnWGBGvMi89yXXs5nfJ8Q5PlLGIHitncT0TmrLlDiBeYObmjFE8kA")
    @GET("/aktivasimaterial/getNomorMaterialForAktivasi")
    suspend fun getNomorMaterialForAktivasi(): Response<GetNomorMaterialForAktivasiResponse>
}