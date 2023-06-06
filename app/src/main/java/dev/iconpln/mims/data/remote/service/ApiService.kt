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
    @POST("/users/loginSso")
    suspend fun loginSso(
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
        @Field("user_loc") userLoc: String,
        @Field("user_name") username: String
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
        @Field("user_loc") userLoc: String,
        @Field("role_id") roleId: Int,
        @Field("user_name") username: String
    ): Response<SnResponse>

    @FormUrlEncoded
    @POST("/reports/permintaan/deleteSn")
    suspend fun permintaanDeleteSn(
        @Field("no_repackaging") noRepackaging: String,
        @Field("no_material") noMaterial: String,
        @Field("serial_number") serialNumber: String,
        @Field("role_id") roleId: Int
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

    @FormUrlEncoded
    @POST("/penerimaan/getDokumentasiPenerimaan")
    suspend fun getDokumentasi(
        @Field("no_do") noDo: String
    ): Response<DocumentationResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MSxcInVzZXJfbmFtZVwiOlwiNTI1MDQuQUdEXCIsXCJ1c2VyX3BsYW50XCI6XCI1MjIxXCIsXCJ1c2VyX3Nsb2NcIjpcIjI5MTBcIixcInVzZXJfcm9sZVwiOlwiN1wiLFwiZW1haWxcIjpcImluamVjdGVkdXNlcjFAaW5qZWN0LmNvbVwifSIsImV4cCI6MTcxNzU4MjEyMiwiaWF0IjoxNjg2MDQ2MTIyfQ.oM0jInFfT0Fg8Bx4YHP02v06rpUO65P292jjsQQCJmJJGicyHl8W5hcIKOZpqKjaU27hGe3n3lDeskMC5GXfWw")
    @GET("/aktivasimaterial/getMonitoringAktivasiMaterial")
    suspend fun getMonitoringAktivasiMaterial(
        @Query("status") status: String,
        @Query("filter") sn: String
    ): Response<MonitoringAktivasiMaterialResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MSxcInVzZXJfbmFtZVwiOlwiNTI1MDQuQUdEXCIsXCJ1c2VyX3BsYW50XCI6XCI1MjIxXCIsXCJ1c2VyX3Nsb2NcIjpcIjI5MTBcIixcInVzZXJfcm9sZVwiOlwiN1wiLFwiZW1haWxcIjpcImluamVjdGVkdXNlcjFAaW5qZWN0LmNvbVwifSIsImV4cCI6MTcxNzU4MjEyMiwiaWF0IjoxNjg2MDQ2MTIyfQ.oM0jInFfT0Fg8Bx4YHP02v06rpUO65P292jjsQQCJmJJGicyHl8W5hcIKOZpqKjaU27hGe3n3lDeskMC5GXfWw")
    @POST("/aktivasimaterial/insertMaterialRegistrasi")
    suspend fun insertMaterialRegistrasi(
        @Body requestBody: RequestBodyRegisSn
    ): Response<InsertMaterialRegistrasiResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MSxcInVzZXJfbmFtZVwiOlwiNTI1MDQuQUdEXCIsXCJ1c2VyX3BsYW50XCI6XCI1MjIxXCIsXCJ1c2VyX3Nsb2NcIjpcIjI5MTBcIixcInVzZXJfcm9sZVwiOlwiN1wiLFwiZW1haWxcIjpcImluamVjdGVkdXNlcjFAaW5qZWN0LmNvbVwifSIsImV4cCI6MTcxNzU4MjEyMiwiaWF0IjoxNjg2MDQ2MTIyfQ.oM0jInFfT0Fg8Bx4YHP02v06rpUO65P292jjsQQCJmJJGicyHl8W5hcIKOZpqKjaU27hGe3n3lDeskMC5GXfWw")
    @GET("/aktivasimaterial/getMaterialAktivasi")
    suspend fun getMaterialAktivasi(
        @Query("status") status: String
    ): Response<GetMaterialAktivasiResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MSxcInVzZXJfbmFtZVwiOlwiNTI1MDQuQUdEXCIsXCJ1c2VyX3BsYW50XCI6XCI1MjIxXCIsXCJ1c2VyX3Nsb2NcIjpcIjI5MTBcIixcInVzZXJfcm9sZVwiOlwiN1wiLFwiZW1haWxcIjpcImluamVjdGVkdXNlcjFAaW5qZWN0LmNvbVwifSIsImV4cCI6MTcxNzU4MjEyMiwiaWF0IjoxNjg2MDQ2MTIyfQ.oM0jInFfT0Fg8Bx4YHP02v06rpUO65P292jjsQQCJmJJGicyHl8W5hcIKOZpqKjaU27hGe3n3lDeskMC5GXfWw")
    @POST("/aktivasimaterial/aktivasiSerialNumber")
    suspend fun aktivasiMaterial(
        @Body requestBody: RequestBodyAktivMaterial
    ): Response<AktivasiSerialNumberResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MSxcInVzZXJfbmFtZVwiOlwiNTI1MDQuQUdEXCIsXCJ1c2VyX3BsYW50XCI6XCI1MjIxXCIsXCJ1c2VyX3Nsb2NcIjpcIjI5MTBcIixcInVzZXJfcm9sZVwiOlwiN1wiLFwiZW1haWxcIjpcImluamVjdGVkdXNlcjFAaW5qZWN0LmNvbVwifSIsImV4cCI6MTcxNzU4MjEyMiwiaWF0IjoxNjg2MDQ2MTIyfQ.oM0jInFfT0Fg8Bx4YHP02v06rpUO65P292jjsQQCJmJJGicyHl8W5hcIKOZpqKjaU27hGe3n3lDeskMC5GXfWw")
    @GET("/aktivasimaterial/getMaterialRegistrasiDetailByDate")
    suspend fun getMaterialRegistrasiDetailByDate(
        @Query("tgl_registrasi") tgl_registrasi: String,
        @Query("status") status: String,
        @Query("filter") sn: String,
        @Query("nomor_material") no_material: String
    ): Response<GetMaterialRegistrasiDetailByDateResponse>

    @Headers("Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MSxcInVzZXJfbmFtZVwiOlwiNTI1MDQuQUdEXCIsXCJ1c2VyX3BsYW50XCI6XCI1MjIxXCIsXCJ1c2VyX3Nsb2NcIjpcIjI5MTBcIixcInVzZXJfcm9sZVwiOlwiN1wiLFwiZW1haWxcIjpcImluamVjdGVkdXNlcjFAaW5qZWN0LmNvbVwifSIsImV4cCI6MTcxNzU4MjEyMiwiaWF0IjoxNjg2MDQ2MTIyfQ.oM0jInFfT0Fg8Bx4YHP02v06rpUO65P292jjsQQCJmJJGicyHl8W5hcIKOZpqKjaU27hGe3n3lDeskMC5GXfWw")
    @GET("/aktivasimaterial/getNomorMaterialForAktivasi")
    suspend fun getNomorMaterialForAktivasi(): Response<GetNomorMaterialForAktivasiResponse>
}