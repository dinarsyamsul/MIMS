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
        return "$BASE_URL/reports/v2/sendReportPenerimaan"
    }

    fun insertLokasi(): String {
        return "$BASE_URL/reports/insertLokasi"
    }

    fun sendPemeriksaan(): String {
        return "$BASE_URL/reports/sendReportPemeriksaan"
    }

    fun sendComplaint(): String {
        return "$BASE_URL/reports/v2/sendReportComplaint"
    }

    fun sendRating():String{
        return "$BASE_URL/reports/v2/sendReportRating"
    }
}