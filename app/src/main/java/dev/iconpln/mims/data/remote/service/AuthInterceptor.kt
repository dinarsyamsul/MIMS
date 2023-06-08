package dev.iconpln.mims.data.remote.service

import android.content.Context
import dev.iconpln.mims.utils.SessionManager
import dev.iconpln.mims.utils.SharedPrefsUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    val session = SessionManager(context)
    val ctx = context

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = SharedPrefsUtils.getStringPreference(ctx,"jwt","")
        val tokenWeb = SharedPrefsUtils.getStringPreference(ctx, "jwtWeb","")
        requestBuilder.addHeader("jwt", "$token")
        requestBuilder.addHeader("Authorization", "Bearer $tokenWeb")

        return chain.proceed(requestBuilder.build())
    }
}