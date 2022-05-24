package com.example.ifpstaff.retrofitService

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

object RetrofitClientInstance {

    lateinit var retrofit: Retrofit

    private const val BASE_URL = Endpoint.Constants.BASE_URL

    private var token = ""

    val retrofitInstance: Retrofit
        get() {
            if (!this::retrofit.isInitialized) {
                val headersInterceptor = Interceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("APIkey",Endpoint.Constants.API_KEY )
                        .addHeader("Content-Type", "application/json")
                        //.addHeader("Authorization", "Bearer $token") //این دستور تا نوشتن بخش ورود به سیستم کارکردی ندارد
                    chain.proceed(requestBuilder.build())
                }
                val okHttpClient = OkHttpClient()
                    .newBuilder()
                    .followRedirects(true)
                    .addInterceptor(headersInterceptor)
                    .build()

                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit
        }

    fun setToken(token: String) {
        RetrofitClientInstance.token = token
    }
}