package dev.iconpln.mims.data.remote.service

import android.content.Context
import dev.iconpln.mims.utils.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    val session = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            val token = session.user_token.first() ?: ""
            requestBuilder.addHeader("jwt", "$token")
        }

        return chain.proceed(requestBuilder.build())
    }
}