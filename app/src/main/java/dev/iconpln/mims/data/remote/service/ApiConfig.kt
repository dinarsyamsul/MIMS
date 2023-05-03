package dev.iconpln.mims.data.remote.service

import android.content.Context
import com.google.android.datatransport.backend.cct.BuildConfig
import dev.iconpln.mims.MyApplication
import dev.iconpln.mims.utils.Constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiService(context: Context): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(context))
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun sendPenerimaanPerson(): String {
        return "$BASE_URL/reports/sendReportPenerimaanPerson"
    }

    fun sendPenerimaan(): String {
        return "$BASE_URL/reports/v3/sendReportPenerimaan"
    }

    fun insertLokasi(): String {
        return "$BASE_URL/reports/insertLokasi"
    }

    fun sendPemeriksaanPerson(): String {
        return "$BASE_URL/reports/sendReportPemeriksaanPerson"
    }

    fun sendPemeriksaan(): String {
        return "$BASE_URL/reports/v3/sendReportPemeriksaan"
    }

    fun sendTerimaMonitoringComplaint(): String {
        return "$BASE_URL/reports/v3/sendReportTerimaMonitoringComplaint"
    }

    fun sendRejectMonitoringComplaint(): String {
        return "$BASE_URL/reports/v3/sendReportRejectMonitoringComplaint"
    }

    fun sendComplaint(): String {
        return "$BASE_URL/reports/v3/sendReportComplaint"
    }

    fun sendComplaintPemeriksaan(): String {
        return "$BASE_URL/reports/v3/sendReportComplaintPemeriksaan"
    }

    fun sendRating():String{
        return "$BASE_URL/reports/v3/sendReportRating"
    }

    fun sendMonkitoringPermintaan():String{
        return "$BASE_URL/reports/sendReportMonitoringPermintaan2"
    }

    fun sendReportPenerimaanUlpDetail():String{
        return "$BASE_URL/reports/penerimaan/updateSelesaiDetail"
    }

    fun sendReportPenerimaanUlpSelesai():String{
        return "$BASE_URL/reports/penerimaan/updateSelesai"
    }

    fun sendReportPemakaianUlpDetail():String{
        return "$BASE_URL/reports/pemakaian/updateSelesaiDetail"
    }

    fun sendReportPemakaianUlpSelesai():String{
        return "$BASE_URL/reports/pemakaian/updateSelesai"
    }

    fun sendReportPemakai():String{
        return "$BASE_URL/reports/pemakaian/insertPemakai"
    }

    fun sendReportPetugasPemeriksaanUlp():String{
        return "$BASE_URL/reports/penerimaanUlp/updatePetugasPemeriksaanUlp"
    }

    fun sendReportPetugasPenerimaanUlp():String{
        return "$BASE_URL/reports/penerimaanUlp/updatePetugasPenerimaanUlp"
    }

}