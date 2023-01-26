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
            requestBuilder.addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VyX2lkXCI6MSxcInVzZXJfbmFtZVwiOlwiMDExMTAuUEFCUklLXCJ9IiwiZXhwIjoxNzA2MDA0Njc4LCJpYXQiOjE2NzQ0Njg2Nzh9.t5meYV9zIFAcOnioH45TR7skXPHLYkTEeoKK59a4l1q46cCDXixFTSSQ-4ET9bpQU7ZMAnDvN_kcQIkWLnkUVw")
        }

        return chain.proceed(requestBuilder.build())
    }
}