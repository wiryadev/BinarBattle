package com.wiryadev.binarbattle.network

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {

    private const val BASE_URL = "https://binar-gdd-cc8.herokuapp.com/api/v1/"

    fun getApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = retrofit {
            baseUrl(BASE_URL)
            addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }

        return retrofit.create()
    }

    private inline fun retrofit(builder: Retrofit.Builder.() -> Unit): Retrofit {
        return Retrofit.Builder()
            .apply { builder() }
            .build()
    }
}