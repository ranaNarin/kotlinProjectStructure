package com.alpha.alphamoney.api

import android.util.Log
import com.alpha.alphamoney.utils.Constants.TAG
import com.alpha.alphamoney.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {


    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e(TAG, "Intercept ...")
        val request = chain.request().newBuilder()
        val token =tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}