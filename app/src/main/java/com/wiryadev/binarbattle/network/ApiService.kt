package com.wiryadev.binarbattle.network

import com.wiryadev.binarbattle.entity.LoginResponse
import com.wiryadev.binarbattle.entity.RegisterResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/register")
    fun register(@Body requestBody: RequestBody): Observable<RegisterResponse>

    @POST("auth/login")
    fun login(@Body requestBody: RequestBody): Observable<LoginResponse>

}